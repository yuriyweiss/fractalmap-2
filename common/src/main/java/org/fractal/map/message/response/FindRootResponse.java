package org.fractal.map.message.response;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class FindRootResponse extends ServletMessage implements Response {

    private int polynomialDegree;

    public FindRootResponse() {
    }

    public FindRootResponse( UUID requestUUID, int polynomialDegree ) {
        super( requestUUID );
        this.polynomialDegree = polynomialDegree;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.RESPONSE_FIND_ROOT;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        super.readFromByteBuffer( buffer );
        polynomialDegree = buffer.getInt();
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // polynomialDegree
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( polynomialDegree );
    }

    public int getPolynomialDegree() {
        return polynomialDegree;
    }

    @Override
    public String toString() {
        return "FindRootResponse{" +
                "polynomialDegree=" + polynomialDegree +
                '}';
    }
}
