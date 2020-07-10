package org.fractal.map.message.response;

import java.nio.ByteBuffer;

public class SquareInfo {

    private double leftRe;
    private double topIm;
    private int canvasLeftX;
    private int canvasTopY;

    public SquareInfo() {
    }

    public SquareInfo( double leftRe, double topIm, int canvasLeftX, int canvasTopY ) {
        this.leftRe = leftRe;
        this.topIm = topIm;
        this.canvasLeftX = canvasLeftX;
        this.canvasTopY = canvasTopY;
    }

    public void readFromByteBuffer( ByteBuffer buffer ) {
        leftRe = buffer.getDouble();
        topIm = buffer.getDouble();
        canvasLeftX = buffer.getInt();
        canvasTopY = buffer.getInt();
    }

    public int estimateLength() {
        int result = 8; // leftRe
        result += 8; // topIm
        result += 4; // canvasLeftX
        result += 4; // canvasTopY
        return result;
    }

    public void writeToByteBuffer( ByteBuffer buffer ) {
        buffer.putDouble( leftRe );
        buffer.putDouble( topIm );
        buffer.putInt( canvasLeftX );
        buffer.putInt( canvasTopY );
    }

    public double getLeftRe() {
        return leftRe;
    }

    public double getTopIm() {
        return topIm;
    }

    public int getCanvasLeftX() {
        return canvasLeftX;
    }

    public int getCanvasTopY() {
        return canvasTopY;
    }

    @Override
    public String toString() {
        return "SquareInfo [leftRe=" + leftRe + ", topIm=" + topIm + ", canvasLeftX=" + canvasLeftX
                + ", canvasTopY=" + canvasTopY + "]";
    }
}
