package org.fractal.map.web.response;

public class SquareInfo {

    private final double leftRe;
    private final double topIm;
    private final int canvasLeftX;
    private final int canvasTopY;

    public SquareInfo( double leftRe, double topIm, int canvasLeftX, int canvasTopY ) {
        this.leftRe = leftRe;
        this.topIm = topIm;
        this.canvasLeftX = canvasLeftX;
        this.canvasTopY = canvasTopY;
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
}
