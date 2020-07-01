package org.fractal.map.tests.transceiver.message;

import org.fractal.map.transceiver.Transportable;

import java.nio.ByteBuffer;

public class ShortDummyMessage extends Transportable {

    private long messageIndex;
    private String messageString;
    // Not serialized.
    private int stringLength;

    public ShortDummyMessage() {
    }

    public ShortDummyMessage( long messageIndex, String messageString ) {
        this.messageIndex = messageIndex;
        this.messageString = messageString;
        this.stringLength = messageString.getBytes().length;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.SHORT_DUMMY;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        messageIndex = buffer.getLong();
        stringLength = buffer.getInt();
        byte[] bytes = new byte[stringLength];
        buffer.get( bytes );
        messageString = new String( bytes );
    }

    @Override
    public int estimateLength() {
        int result = HEADER_LENGTH;
        result += 8; // messageIndex size
        result += 4; // stringLength size
        result += messageString.getBytes().length;
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putLong( messageIndex );
        buffer.putInt( stringLength );
        buffer.put( messageString.getBytes() );
    }

    public long getMessageIndex() {
        return messageIndex;
    }

    public String getMessageString() {
        return messageString;
    }

    @Override
    public String toString() {
        return "ShortDummyMessage [messageIndex=" + messageIndex + ", messageString="
                + messageString + "]";
    }

    @Override
    public String getIgnoredMessageInfo() {
        return "ShortDummyMessage [messageIndex=" + messageIndex + "]";
    }
}
