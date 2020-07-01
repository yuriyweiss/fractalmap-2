package org.fractal.map.tests.transceiver.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.util.buffer.Buffer;

import java.nio.ByteBuffer;

public class LongDummyMessage extends Transportable {

    private static final Logger logger = LogManager.getLogger();

    private long messageIndex;
    private Buffer buffer;

    public LongDummyMessage() {
    }

    public LongDummyMessage( long messageIndex, Buffer buffer ) {
        this.messageIndex = messageIndex;
        this.buffer = buffer;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.LONG_DUMMY;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer inputBuffer ) {
        messageIndex = inputBuffer.getLong();
        int arrayLength = inputBuffer.getInt();
        try {
            buffer = LongMessageBufferPool.borrowObject();
            logger.trace( "buffer borrowed for message: {}", messageIndex );
        } catch ( Exception e ) {
            int currPosition = inputBuffer.position();
            inputBuffer.position( currPosition + arrayLength );
            throw new RuntimeException( "Long message buffer borrow exception.", e );
        }
        buffer.setUsedCount( arrayLength );
        inputBuffer.get( buffer.getBytes(), 0, arrayLength );
    }

    @Override
    public int estimateLength() {
        int result = HEADER_LENGTH;
        result += 8; // messageIndex size
        result += 4; // arrayLength size
        result += buffer.getUsedCount();
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer outputBuffer ) {
        super.writeToByteBuffer( outputBuffer );
        outputBuffer.putLong( messageIndex );
        outputBuffer.putInt( buffer.getUsedCount() );
        outputBuffer.put( buffer.getBytes(), 0, buffer.getUsedCount() );
    }

    public long getMessageIndex() {
        return messageIndex;
    }

    public byte[] getBytes() {
        return buffer.getBytes();
    }

    public void setMessageLength( int messageLength ) {
        buffer.setUsedCount( messageLength );
    }

    public int getMessageLength() {
        return buffer.getUsedCount();
    }

    @Override
    public String toString() {
        return "LongDummyMessage [messageIndex=" + messageIndex + "]";
    }

    @Override
    public void releaseResources() {
        try {
            LongMessageBufferPool.returnObject( buffer );
            logger.trace( "buffer released for message: {}", messageIndex );
        } catch ( Exception e ) {
            logger.error( "releasing message buffer failed" );
            logger.debug( "error stack: ", e );
        }
    }

    @Override
    public String getIgnoredMessageInfo() {
        return "LongDummyMessage [messageIndex=" + messageIndex + "]";
    }
}
