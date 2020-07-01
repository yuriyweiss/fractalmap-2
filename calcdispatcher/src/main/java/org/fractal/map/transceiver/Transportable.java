package org.fractal.map.transceiver;

import java.nio.ByteBuffer;

public abstract class Transportable {

    public static final int HEADER_LENGTH = 4;

    public abstract int getClassId();

    public abstract void readFromByteBuffer( ByteBuffer buffer );

    public abstract int estimateLength();

    public abstract String getIgnoredMessageInfo();

    public void writeToByteBuffer( ByteBuffer buffer ) {
        buffer.putInt( estimateLength() ); //ignore in length calculation
        buffer.putInt( getClassId() );
    }

    public void releaseResources() {
        // Override if necessary.
        // Release resources after message was written to outputBuffer.
    }
}
