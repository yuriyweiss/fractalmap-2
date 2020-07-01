package org.fractal.map.message.response;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class PointCoordsResponse extends ServletMessage implements Response {

    private double re;
    private double im;

    public PointCoordsResponse() {
    }

    public PointCoordsResponse( UUID requestUUID, double re, double im ) {
        super( requestUUID );
        this.re = re;
        this.im = im;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.RESPONSE_POINT_COORDS;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        super.readFromByteBuffer( buffer );
        re = buffer.getDouble();
        im = buffer.getDouble();
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 8; // re
        result += 8; // im
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putDouble( re );
        buffer.putDouble( im );
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public String toString() {
        return "PointCoordsResponse [re=" + re + ", im=" + im + ", getRequestUUID()="
                + getRequestUUID() + "]";
    }
}
