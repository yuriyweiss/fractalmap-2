package org.fractal.map.web.response;

public class SquareBodyInfo extends AbstractInfo {

    private int iterations = -2;
    private int[][] points = null;

    public SquareBodyInfo() {
    }

    public SquareBodyInfo( int iterations ) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return iterations;
    }

    public void setPoints( int[][] points ) {
        this.points = points;
    }

    public int[][] getPoints() {
        return points;
    }
}
