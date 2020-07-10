package org.fractal.map.tests.transceiver;

import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.conf.Configuration;
import org.fractal.map.monitor.MonitorLoggingMode;
import org.fractal.map.monitor.MonitorThread;
import org.fractal.map.tests.transceiver.message.LongMessageBufferPool;
import org.fractal.map.tests.transceiver.message.MessagesRegistrator;
import org.fractal.map.transceiver.server.TransceiverServer;
import org.fractal.map.util.ThreadUtils;

public class Server {

    private static MonitorThread monitorThread = null;
    private static TransceiverServer transceiverServer = null;

    public static void main( String[] args ) throws Exception {
        System.setProperty( "log4j.configurationFile", "conf/log4j2-trans-server.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/calcdisp.conf" );
        LongMessageBufferPool.init( 200000, 100 );
        MessagesRegistrator.registerMessages();

        monitorThread = new MonitorThread( MonitorLoggingMode.TRANSCEIVER );

        transceiverServer =
                new TransceiverServer( Configuration.getTransceiverReadPort(),
                        Configuration.getTransceiverWritePort(),
                        Configuration.getTransceiverBufferSize(),
                        Configuration.getTransceiverErrorIntervalSeconds() );
        transceiverServer.setMessageProcessor( new DummyServerMessageProcessor( transceiverServer ) );
        transceiverServer.start();

        monitorThread.start();

        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                Server.stopApplication();
                ThreadUtils.sleep( 1000 );
            }
        } );
    }

    public static void stopApplication() {
        if ( monitorThread != null ) {
            monitorThread.terminate();
        }
        if ( transceiverServer != null ) {
            transceiverServer.stop();
        }
    }
}
