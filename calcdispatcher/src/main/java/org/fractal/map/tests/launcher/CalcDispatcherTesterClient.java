package org.fractal.map.tests.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.conf.Configuration;
import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.request.PointCoordsRequest;
import org.fractal.map.monitor.MonitorLoggingMode;
import org.fractal.map.monitor.MonitorThread;
import org.fractal.map.tests.launcher.kpi.WaitersCacheSizeKpi;
import org.fractal.map.transceiver.client.TransceiverClient;
import org.fractal.map.util.ThreadUtils;
import org.fractal.map.waiterscache.WaitersCache;

import java.util.Random;
import java.util.UUID;

public class CalcDispatcherTesterClient {

    @SuppressWarnings( "unused" )
    private static final Logger logger = LogManager.getLogger();

    public static void main( String[] args ) throws Exception {
        System.setProperty( "log4j.configurationFile", "conf/log4j2-calcdisp-tester.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/calcdisp.conf" );
        MessagesRegistrator.registerMessages();

        MonitorThread monitorThread = new MonitorThread( MonitorLoggingMode.TRANSCEIVER );

        WaitersCache waiters = new WaitersCache( 60 );
        monitorThread.registerAdditionalKpi( new WaitersCacheSizeKpi( waiters ) );

        String ipAddress = "127.0.0.1";
        // Flip read/write ports to connect to server.
        TransceiverClient transceiverClient =
                new TransceiverClient( Configuration.getTransceiverWritePort(), Configuration.getTransceiverReadPort(),
                        ipAddress );
        transceiverClient.setMessageProcessor( new ResponseMessageProcessor( transceiverClient, waiters ) );
        transceiverClient.start();

        monitorThread.start();

        Random random = new Random();
        while ( true ) {
            double re = -1.5 + 2 * random.nextDouble();
            double im = 1 - 2 * random.nextDouble();
            int shiftX = -250 + random.nextInt( 500 );
            int shiftY = -200 + random.nextInt( 400 );
            PointCoordsRequest request =
                    new PointCoordsRequest( UUID.randomUUID(), 5, re, im, shiftX, shiftY );
            waiters.put( request.getRequestUUID(), new DummyWaiter() );
            transceiverClient.send( request );
            // long sleepTime = random.nextInt(10) + 10;
            long sleepTime = 1000L;
            ThreadUtils.sleep( sleepTime );
        }
    }
}
