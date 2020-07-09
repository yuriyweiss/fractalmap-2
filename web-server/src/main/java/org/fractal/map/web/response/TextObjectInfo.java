package org.fractal.map.web.response;

public class TextObjectInfo {

    private final double re;
    private final double im;
    private final int canvasLeftX;
    private final int canvasTopY;
    private final String text;

    public TextObjectInfo( double re, double im, int canvasLeftX, int canvasTopY, String text ) {
        this.re = re;
        this.im = im;
        this.canvasLeftX = canvasLeftX;
        this.canvasTopY = canvasTopY;
        this.text = text;
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
}
