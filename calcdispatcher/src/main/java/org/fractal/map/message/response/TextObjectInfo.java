package org.fractal.map.message.response;

import java.nio.ByteBuffer;

public class TextObjectInfo {

    private double re;
    private double im;
    private int canvasX;
    private int canvasY;
    private String text;

    public TextObjectInfo() {
    }

    public TextObjectInfo( double re, double im, String text ) {
        this.re = re;
        this.im = im;
        this.text = text;
    }

    public void readFromByteBuffer( ByteBuffer buffer ) {
        re = buffer.getDouble();
        im = buffer.getDouble();
        canvasX = buffer.getInt();
        canvasY = buffer.getInt();
        int textLength = buffer.getInt();
        byte[] textBytes = new byte[textLength];
        buffer.get( textBytes );
        text = new String( textBytes );
    }

    public int estimateLength() {
        int result = 8; // leftRe
        result += 8; // topIm
        result += 4; // canvasX
        result += 4; // canvasY
        result += 4; // textLength
        result += text.getBytes().length; // text.bytes.length
        return result;
    }

    public void writeToByteBuffer( ByteBuffer buffer ) {
        buffer.putDouble( re );
        buffer.putDouble( im );
        buffer.putInt( canvasX );
        buffer.putInt( canvasY );
        buffer.putInt( text.getBytes().length );
        buffer.put( text.getBytes() );
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public int getCanvasX() {
        return canvasX;
    }

    public int getCanvasY() {
        return canvasY;
    }

    public String getText() {
        return text;
    }

    public void setCanvasX( int canvasX ) {
        this.canvasX = canvasX;
    }

    public void setCanvasY( int canvasY ) {
        this.canvasY = canvasY;
    }

    @Override
    public String toString() {
        return "TextObjectInfo{" +
                "re=" + re +
                ", im=" + im +
                ", canvasX=" + canvasX +
                ", canvasY=" + canvasY +
                ", text='" + text + '\'' +
                '}';
    }
}
