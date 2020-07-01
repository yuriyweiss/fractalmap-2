package org.fractal.map.message.response;

import org.fractal.map.calc.Constants;
import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class SquareResponse extends ServletMessage implements Response {

    private int iterations;
    private byte[] squareBody = null;

    public SquareResponse() {
    }

    public SquareResponse( UUID requestUUID, int iterations ) {
        super( requestUUID );
        this.iterations = iterations;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.RESPONSE_SQUARE;
    }

    public void setSquareBody( byte[] squareBody ) {
        this.squareBody = squareBody;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        squareBody = null;
        super.readFromByteBuffer( buffer );
        iterations = buffer.getInt();
        if ( iterations == Constants.ITERATIONS_DIFFER ) {
            int bodyLength = buffer.getInt();
            squareBody = new byte[bodyLength];
            buffer.get( squareBody );
        }
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // iterations
        if ( iterations == Constants.ITERATIONS_DIFFER ) {
            result += 4; // body length
            result += squareBody.length; // bytes count
        }
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( iterations );
        if ( iterations == Constants.ITERATIONS_DIFFER ) {
            buffer.putInt( squareBody.length );
            buffer.put( squareBody );
        }
    }

    public int getIterations() {
        return iterations;
    }

    public byte[] getSquareBody() {
        return squareBody;
    }

    @Override
    public String toString() {
        return "SquareResponse [iterations=" + iterations + ", getRequestUUID()="
                + getRequestUUID() + "]";
    }
}
