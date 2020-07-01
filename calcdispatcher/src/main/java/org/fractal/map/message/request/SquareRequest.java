package org.fractal.map.message.request;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class SquareRequest extends ServletMessage implements Request {

    private int layerIndex;
    private double leftRe;
    private double topIm;

    public SquareRequest() {
    }

    public SquareRequest( UUID requestUUID, int layerIndex, double leftRe, double topIm ) {
        super( requestUUID );
        this.layerIndex = layerIndex;
        this.leftRe = leftRe;
        this.topIm = topIm;
    }

    @Override
    public boolean isCalcRequest() {
        return false;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.REQUEST_SQUARE;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        super.readFromByteBuffer( buffer );
        layerIndex = buffer.getInt();
        leftRe = buffer.getDouble();
        topIm = buffer.getDouble();
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // layerIndex
        result += 8; // leftRe
        result += 8; // topIm
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( layerIndex );
        buffer.putDouble( leftRe );
        buffer.putDouble( topIm );
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public double getLeftRe() {
        return leftRe;
    }

    public double getTopIm() {
        return topIm;
    }

    @Override
    public String toString() {
        return "SquareRequest [layerIndex=" + layerIndex + ", leftRe=" + leftRe + ", topIm="
                + topIm + ", getRequestUUID()=" + getRequestUUID() + "]";
    }
}
