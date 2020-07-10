package org.fractal.map.message.request;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class GetAreaTextObjectsRequest extends ServletMessage implements Request {

    private int layerIndex;
    private double re;
    private double im;
    private int areaSizeX;
    private int areaSizeY;

    public GetAreaTextObjectsRequest() {
    }

    public GetAreaTextObjectsRequest( UUID requestUUID, int layerIndex, double re, double im,
            int areaSizeX, int areaSizeY ) {
        super( requestUUID );
        this.layerIndex = layerIndex;
        this.re = re;
        this.im = im;
        this.areaSizeX = areaSizeX;
        this.areaSizeY = areaSizeY;
    }

    @Override
    public boolean isCalcRequest() {
        return true;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.REQUEST_GET_AREA_TEXT_OBJECTS;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        super.readFromByteBuffer( buffer );
        layerIndex = buffer.getInt();
        re = buffer.getDouble();
        im = buffer.getDouble();
        areaSizeX = buffer.getInt();
        areaSizeY = buffer.getInt();
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // layerIndex
        result += 8; // re
        result += 8; // im
        result += 4; // areaSizeX
        result += 4; // areaSizeY
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( layerIndex );
        buffer.putDouble( re );
        buffer.putDouble( im );
        buffer.putInt( areaSizeX );
        buffer.putInt( areaSizeY );
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

    public int getAreaSizeX() {
        return areaSizeX;
    }

    public int getAreaSizeY() {
        return areaSizeY;
    }

    @Override
    public String toString() {
        return "GetAreaTextObjectsRequest{" +
                "layerIndex=" + layerIndex +
                ", re=" + re +
                ", im=" + im +
                ", areaSizeX=" + areaSizeX +
                ", areaSizeY=" + areaSizeY +
                '}';
    }
}
