package org.fractal.map.message;

import org.fractal.map.transceiver.Transportable;

import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class ServletMessage extends Transportable {

    private UUID requestUUID;

    public ServletMessage() {
    }

    public ServletMessage( UUID requestUUID ) {
        this.requestUUID = requestUUID;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        long mostSigBits = buffer.getLong();
        long leastSigBits = buffer.getLong();
        requestUUID = new UUID( mostSigBits, leastSigBits );
    }

    @Override
    public int estimateLength() {
        int result = HEADER_LENGTH;
        result += 2 * 8; // requestUUID = 2 long
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putLong( requestUUID.getMostSignificantBits() );
        buffer.putLong( requestUUID.getLeastSignificantBits() );
    }

    @Override
    public String getIgnoredMessageInfo() {
        return getClass().getSimpleName() + " [requestUUID=" + requestUUID + "]";
    }

    @Override
    public String toString() {
        return "ServletMessage [requestUUID=" + requestUUID + "]";
    }
}
