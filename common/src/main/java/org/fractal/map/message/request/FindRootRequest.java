package org.fractal.map.message.request;

import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

public class FindRootRequest extends ServletMessage implements Request {

    private int layerIndex;
    private double leftRe;
    private double rightRe;
    private double topIm;
    private double bottomIm;

    public FindRootRequest() {
    }

    public FindRootRequest( UUID requestUUID, int layerIndex, double leftRe, double topIm, double rightRe,
            double bottomIm ) {
        super( requestUUID );
        this.layerIndex = layerIndex;
        this.leftRe = leftRe;
        this.topIm = topIm;
        this.rightRe = rightRe;
        this.bottomIm = bottomIm;
    }

    @Override
    public boolean isCalcRequest() {
        return true;
    }

    @Override
    public int getClassId() {
        return MessagesRegistrator.REQUEST_FIND_ROOT;
    }

    @Override
    public void readFromByteBuffer( ByteBuffer buffer ) {
        super.readFromByteBuffer( buffer );
        layerIndex = buffer.getInt();
        leftRe = buffer.getDouble();
        topIm = buffer.getDouble();
        rightRe = buffer.getDouble();
        bottomIm = buffer.getDouble();
    }

    @Override
    public int estimateLength() {
        int result = super.estimateLength();
        result += 4; // layerIndex
        result += 8; // leftRe
        result += 8; // topIm
        result += 8; // rightRe
        result += 8; // bottomIm
        return result;
    }

    @Override
    public void writeToByteBuffer( ByteBuffer buffer ) {
        super.writeToByteBuffer( buffer );
        buffer.putInt( layerIndex );
        buffer.putDouble( leftRe );
        buffer.putDouble( topIm );
        buffer.putDouble( rightRe );
        buffer.putDouble( bottomIm );
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public double getLeftRe() {
        return leftRe;
    }

    public double getRightRe() {
        return rightRe;
    }

    public double getTopIm() {
        return topIm;
    }

    public double getBottomIm() {
        return bottomIm;
    }

    @Override
    public String toString() {
        return "FindRootRequest{" +
                "layerIndex=" + layerIndex +
                ", leftRe=" + leftRe +
                ", rightRe=" + rightRe +
                ", topIm=" + topIm +
                ", bottomIm=" + bottomIm +
                '}';
    }
}
