package org.fractal.map.web.response;

public class TextObjectInfo {

    private final double re;
    private final double im;
    private final int canvasX;
    private final int canvasY;
    private final String text;

    public TextObjectInfo( double re, double im, int canvasX, int canvasY, String text ) {
        this.re = re;
        this.im = im;
        this.canvasX = canvasX;
        this.canvasY = canvasY;
        this.text = text;
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
}
