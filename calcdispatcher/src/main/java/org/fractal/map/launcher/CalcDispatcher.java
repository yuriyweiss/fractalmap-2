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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CalcDispatcher {

    private static final Logger logger = LogManager.getLogger();

    private static MonitorThread monitorThread = null;
    private static TransceiverServer transceiverServer = null;
    private static ThreadPoolExecutor calcExecutor;
    private static ThreadPoolExecutor squareExecutor;

    public static void main( String[] args ) throws Exception {
        System.setProperty( "log4j.configurationFile", "conf/log4j2-calcdisp.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/calcdisp.conf" );

        MessagesRegistrator.registerMessages();

        monitorThread = new MonitorThread( MonitorLoggingMode.CALC | MonitorLoggingMode.TRANSCEIVER );

        calcExecutor = createProcessingExecutor( "calcExecutor", 10, 20, 10000 );
        squareExecutor = createProcessingExecutor( "squareExecutor", 10, 100, 5000 );
        monitorThread.registerAdditionalKpi( new SquareQueueSizeKpi( squareExecutor ) );

        transceiverServer =
                new TransceiverServer( Configuration.getTransceiverReadPort(),
                        Configuration.getTransceiverWritePort() );
        transceiverServer.setMessageProcessor(
                new IncomingMessageProcessor( calcExecutor, squareExecutor, transceiverServer ) );
        transceiverServer.start();

        monitorThread.start();

        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                CalcDispatcher.stopApplication();
            }
        } );
    }

    private static ThreadPoolExecutor createProcessingExecutor( final String executorName,
            int corePoolSize, int maxPoolSize, int queueCapacity ) {
        ThreadPoolExecutor result =
                new ThreadPoolExecutor( corePoolSize, maxPoolSize, 5000, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>( queueCapacity ) );
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

    public static void stopApplication() {
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

    private static void stopProcessingExecutor( String executorName, ThreadPoolExecutor executor ) {
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
