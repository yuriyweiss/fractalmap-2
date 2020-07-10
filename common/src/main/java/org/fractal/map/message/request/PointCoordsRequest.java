package org.fractal.map.message.request;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class PointCoordsRequest extends ServletMessage implements Request {

    private int layerIndex;
    private double re;
    private double im;
    private int shiftX;
    private int shiftY;

    public PointCoordsRequest() {
    }

    public PointCoordsRequest( UUID requestUUID, int layerIndex, double re, double im, int shiftX,
            int shiftY ) {
        super( requestUUID );
        this.layerIndex = layerIndex;
        this.re = re;
        this.im = im;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    @Override
    public boolean isCalcRequest() {
        return true;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.REQUEST_POINT_COORDS;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        super.readFromByteBuffer( buffer );
        layerIndex = buffer.getInt();
        re = buffer.getDouble();
        im = buffer.getDouble();
        shiftX = buffer.getInt();
        shiftY = buffer.getInt();
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // layerIndex
        result += 8; // re
        result += 8; // im
        result += 4; // shiftX
        result += 4; // shiftY
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( layerIndex );
        buffer.putDouble( re );
        buffer.putDouble( im );
        buffer.putInt( shiftX );
        buffer.putInt( shiftY );
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public int getShiftX() {
        return shiftX;
    }

    public int getShiftY() {
        return shiftY;
    }

    @Override
    public String toString() {
        return "PointCoordsRequest [layerIndex=" + layerIndex + ", re=" + re + ", im=" + im
                + ", shiftX=" + shiftX + ", shiftY=" + shiftY + ", getRequestUUID()="
                + getRequestUUID() + "]";
    }
}
