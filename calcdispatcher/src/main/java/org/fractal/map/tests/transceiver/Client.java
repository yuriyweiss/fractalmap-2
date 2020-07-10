package org.fractal.map.tests.transceiver;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.conf.Configuration;
import org.fractal.map.monitor.MonitorLoggingMode;
import org.fractal.map.monitor.MonitorThread;
import org.fractal.map.tests.transceiver.message.LongDummyMessage;
import org.fractal.map.tests.transceiver.message.LongMessageBufferPool;
import org.fractal.map.tests.transceiver.message.MessagesRegistrator;
import org.fractal.map.tests.transceiver.message.ShortDummyMessage;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.transceiver.client.TransceiverClient;
import org.fractal.map.util.ThreadUtils;
import org.fractal.map.util.buffer.Buffer;

public class Client {

    private static final Logger logger = LogManager.getLogger();

    private static long messageIndex = 1;
    @SuppressWarnings( "unused" )
    private static Random bufferSizeRandom = new Random();

    public static void main( String[] args ) throws Exception {
        System.setProperty( "log4j.configurationFile", "conf/log4j2-trans-client.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/calcdisp.conf" );
        LongMessageBufferPool.init( 200000, 100 );
        MessagesRegistrator.registerMessages();

        MonitorThread monitorThread = new MonitorThread( MonitorLoggingMode.TRANSCEIVER );
        monitorThread.start();

        String ipAddress = "127.0.0.1";
        // Flip read/write ports to connect to server.
        TransceiverClient transceiverClient =
                new TransceiverClient( Configuration.getTransceiverWritePort(),
                        Configuration.getTransceiverReadPort(),
                        ipAddress,
                        Configuration.getTransceiverBufferSize(),
                        Configuration.getTransceiverErrorIntervalSeconds() );
        transceiverClient.setMessageProcessor( new DummyClientMessageProcessor() );
        transceiverClient.start();

        // Random random = new Random();
        while ( true ) {
            for ( int i = 0; i < 10; i++ ) {
                Transportable longMessage = buildLongMessage();
                if ( longMessage != null ) {
                    transceiverClient.send( longMessage );
                }
                messageIndex++;
            }
            for ( int i = 0; i < 50; i++ ) {
                transceiverClient.send( buildShortMessage() );
                messageIndex++;
            }
            // long sleepTime = random.nextInt(10) + 10;
            long sleepTime = 10;
            ThreadUtils.sleep( sleepTime );
        }
    }

    private static Transportable buildLongMessage() {
        Buffer buffer = prepareLongMessageBuffer();
        if ( buffer != null ) {
            return new LongDummyMessage( messageIndex, buffer );
        } else {
            return null;
        }
    }

    private static Buffer prepareLongMessageBuffer() {
        Buffer result = null;
        try {
            result = LongMessageBufferPool.borrowObject();
            logger.trace( "buffer borrowed for message: {}", messageIndex );
        } catch ( Exception e ) {
            logger.error( "buffer borrow failed" );
            logger.debug( "error stack: ", e );
        }
        if ( result == null ) return null;

        // int arrayLength = bufferSizeRandom.nextInt(150000);
        int arrayLength = 100000;
        result.setUsedCount( arrayLength );
        byte[] bytes = result.getBytes();
        for ( int i = 0; i < arrayLength; i++ ) {
            bytes[i] = 25;
        }
        return result;
    }

    private static Transportable buildShortMessage() {
        return new ShortDummyMessage( messageIndex, "1111111111111" );
    }
}
