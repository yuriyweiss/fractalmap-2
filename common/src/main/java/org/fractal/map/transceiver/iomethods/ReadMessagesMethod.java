package org.fractal.map.transceiver.iomethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.transceiver.CommunicationThread;

import java.nio.ByteBuffer;

public class ReadMessagesMethod {

    private static final Logger logger = LogManager.getLogger();

    private final CommunicationThread caller;
    private final ByteBuffer inputBuffer;

    public ReadMessagesMethod( CommunicationThread caller, ByteBuffer inputBuffer ) {
        this.caller = caller;
        this.inputBuffer = inputBuffer;
    }

    public void execute() {
        int position = 0;
        int len = 0;
        int messagesProcessed = 0;
        try {
            if ( logger.isTraceEnabled() ) {
                logger.trace( "received byteBuffer size: {}", inputBuffer.position() );
            }
            // While something is in the buffer. (write mode - check position)
            while ( inputBuffer.position() > 4 ) {
                // Switch writing -> reading.
                inputBuffer.flip();
                // Get next message length, but restore position to append data if incomplete.
                inputBuffer.mark();
                len = inputBuffer.getInt();
                inputBuffer.reset();
                // Exit if package is incomplete. (read mode - check limit)
                if ( inputBuffer.limit() < len + 4 ) {
                    if ( logger.isTraceEnabled() ) {
                        logger.trace( "buffer incomplete" );
                        logger.trace( "waiting: {} bytes, current: {} bytes", len, inputBuffer.limit() );
                    }
                    inputBuffer.compact();
                    break;
                }

                if ( logger.isTraceEnabled() ) {
                    logger.trace( "buffer size: {}", inputBuffer.limit() );
                    logger.trace( "message length: {}", len );
                }
                // Read but ignore 4 bytes of message length.
                inputBuffer.getInt();

                caller.decodeMessage( inputBuffer );
                messagesProcessed++;

                // Switch reading -> writing.
                if ( inputBuffer.hasRemaining() ) {
                    inputBuffer.compact();
                } else {
                    inputBuffer.clear();
                }
            }
        } catch ( RuntimeException e ) {
            logger.trace( "position: {}; len: {}; messagesProcessed: {}",
                    position, Integer.toBinaryString( len ), messagesProcessed );
            throw e;
        }
    }
}
