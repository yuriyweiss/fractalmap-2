package org.fractal.map.model;

import org.fractal.map.calc.Constants;

public class Layer implements Comparable<Layer> {

    private final int layerIndex;
    private final int iterations;
    private final long layerSideSize;
    private final long squaresBySide;
    private final double squareWidth;
    private final double pointWidth;

    public Layer( int layerIndex, int iterations, long layerSideSize ) {
        this.layerIndex = layerIndex;
        this.iterations = iterations;
        this.layerSideSize = layerSideSize;
        this.squaresBySide = layerSideSize / Constants.SQUARE_SIDE_SIZE;
        this.squareWidth = ( Constants.RE_RIGHT - Constants.RE_LEFT ) / ( double ) squaresBySide;
        this.pointWidth = ( Constants.RE_RIGHT - Constants.RE_LEFT ) / ( double ) layerSideSize;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public int getIterations() {
        return iterations;
    }

    public long getSquaresBySide() {
        return squaresBySide;
    }

    public double getSquareWidth() {
        return squareWidth;
    }

    public long getLayerSideSize() {
        return layerSideSize;
    }

    public double getPointWidth() {
        return pointWidth;
    }

    @Override
    public String toString() {
        return "Layer [layerIndex=" + layerIndex + ", iterations=" + iterations
                + ", layerSideSize=" + layerSideSize + ", squaresBySide=" + squaresBySide
                + ", squareWidth=" + squareWidth + ", pointWidth=" + pointWidth + "]";
    }

    @Override
    public int compareTo( Layer other ) {
        return Integer.compare( layerIndex, other.getLayerIndex() );
    }
}
