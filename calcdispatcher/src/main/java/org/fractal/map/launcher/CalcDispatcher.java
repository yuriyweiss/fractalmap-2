package org.fractal.map.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.conf.Configuration;
import org.fractal.map.launcher.kpi.SquareQueueSizeKpi;
import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;
import org.fractal.map.monitor.MonitorLoggingMode;
import org.fractal.map.monitor.MonitorThread;
import org.fractal.map.transceiver.server.ServerMessage;
import org.fractal.map.transceiver.server.TransceiverServer;
import org.fractal.map.util.ThreadUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@ComponentScan( basePackages = { "org.fractal.map" } )
public class CalcDispatcher {

    private static final Logger logger = LogManager.getLogger();

    private MonitorThread monitorThread = null;
    private TransceiverServer transceiverServer = null;
    private ThreadPoolExecutor calcExecutor;
    private ThreadPoolExecutor squareExecutor;

    public static void main( String[] args ) {
        SpringApplication.run( CalcDispatcher.class, args );
    }

    @PostConstruct
    public void launchApplication() throws IOException, ParseException {
        System.setProperty( "log4j.configurationFile", "conf/log4j2-calcdisp.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/calcdisp.conf" );

        MessagesRegistrator.registerMessages();

        monitorThread = new MonitorThread( MonitorLoggingMode.CALC | MonitorLoggingMode.TRANSCEIVER );

        calcExecutor = createProcessingExecutor( "calcExecutor", 20, 10000 );
        squareExecutor = createProcessingExecutor( "squareExecutor", 100, 5000 );
        monitorThread.registerAdditionalKpi( new SquareQueueSizeKpi( squareExecutor ) );

        transceiverServer =
                new TransceiverServer( Configuration.getTransceiverReadPort(),
                        Configuration.getTransceiverWritePort() );
        transceiverServer.setMessageProcessor(
                new IncomingMessageProcessor( calcExecutor, squareExecutor, transceiverServer ) );
        transceiverServer.start();

        monitorThread.start();

        Runtime.getRuntime().addShutdownHook( new Thread( this::stopApplication ) );
    }

    private ThreadPoolExecutor createProcessingExecutor( final String executorName, int maxPoolSize,
            int queueCapacity ) {
        ThreadPoolExecutor result =
                new ThreadPoolExecutor( 10, maxPoolSize, 5000, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>( queueCapacity ) );
        result.setRejectedExecutionHandler( ( task, executor ) -> {
            ServerMessage request = ( ( MessageProcessingTask ) task ).getMessage();
            logMessageIgnored( executorName, ( ServletMessage ) request.getBody() );
        } );
        return result;
    }

    private static void logMessageIgnored( String executorName, ServletMessage message ) {
        logger.warn( "{} message ignored, executor queue full", executorName );
        logger.debug( message.getIgnoredMessageInfo() );
    }

    public void stopApplication() {
        if ( monitorThread != null ) {
            monitorThread.terminate();
        }
        if ( transceiverServer != null ) {
            transceiverServer.stop();
            // wait 10 seconds
            ThreadUtils.sleep( 10000L );
        }
        stopProcessingExecutor( "calcExecutor", calcExecutor );
        stopProcessingExecutor( "squareExecutor", squareExecutor );
    }

    private void stopProcessingExecutor( String executorName, ThreadPoolExecutor executor ) {
        executor.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if ( !executor.awaitTermination( 30, TimeUnit.SECONDS ) ) {
                logger.info( "{} try force shutdown", executorName );
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if ( !executor.awaitTermination( 30, TimeUnit.SECONDS ) ) {
                    logger.error( "{} hadn't terminated at appropriate time", executorName );
                }
            }
            logger.info( "{} shutdown success", executorName );
        } catch ( InterruptedException ie ) {
            // (Re-)Cancel if current thread also interrupted
            logger.info( "{} try force shutdown on interruption", executorName );
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        logger.info( "{} shutdown process finished", executorName );
    }
}
