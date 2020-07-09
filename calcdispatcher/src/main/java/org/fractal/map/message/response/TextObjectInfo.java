package org.fractal.map.message.response;

import java.nio.ByteBuffer;

public class TextObjectInfo {

    private double re;
    private double im;
    private int canvasLeftX;
    private int canvasTopY;
    private String text;

    public void readFromByteBuffer( ByteBuffer buffer ) {
        re = buffer.getDouble();
        im = buffer.getDouble();
        canvasLeftX = buffer.getInt();
        canvasTopY = buffer.getInt();
        int textLength = buffer.getInt();
        byte[] textBytes = new byte[textLength];
        buffer.get( textBytes );
        text = new String( textBytes );
    }

    public int estimateLength() {
        int result = 8; // leftRe
        result += 8; // topIm
        result += 4; // canvasLeftX
        result += 4; // canvasTopY
        result += 4; // textLength
        result += text.getBytes().length; // text.bytes.length
        return result;
    }

    public void writeToByteBuffer( ByteBuffer buffer ) {
        buffer.putDouble( re );
        buffer.putDouble( im );
        buffer.putInt( canvasLeftX );
        buffer.putInt( canvasTopY );
        buffer.putInt( text.getBytes().length );
        buffer.put( text.getBytes() );
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public int getCanvasLeftX() {
        return canvasLeftX;
    }

    public int getCanvasTopY() {
        return canvasTopY;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextObjectInfo{" +
                "re=" + re +
                ", im=" + im +
                ", canvasLeftX=" + canvasLeftX +
                ", canvasTopY=" + canvasTopY +
                ", text='" + text + '\'' +
                '}';
    }
}
