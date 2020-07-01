package org.fractal.map.model;

import org.fractal.map.calc.Constants;
import org.fractal.map.calc.square.SquareCalculatorFactory;

import static org.fractal.map.calc.Constants.SQUARE_SIDE_SIZE;

public class Square implements Comparable<Square> {

    private final Layer layer;
    private final double leftRe;
    private final double topIm;
    private final double width;

    private int iterations = Constants.ITERATIONS_DIFFER;

    // calculated
    private final long layerX;
    private final long layerY;
    private final long squareIndex;

    public Square( int layerIndex, double leftRe, double topIm ) {
        this( LayerRegistry.getLayerByIndex( layerIndex ), leftRe, topIm );
    }

    public Square( Layer layer, double leftRe, double topIm ) {
        this.layer = layer;
        this.leftRe = leftRe;
        this.topIm = topIm;
        this.width = layer.getSquareWidth();

        this.layerX = Math.round( ( leftRe - Constants.RE_LEFT ) / width );
        this.layerY = Math.round( ( Constants.IM_TOP - topIm ) / width );
        this.squareIndex = layerY * layer.getSquaresBySide() + layerX;
    }

    public Layer getLayer() {
        return layer;
    }

    public double getLeftRe() {
        return leftRe;
    }

    public double getTopIm() {
        return topIm;
    }

    public long getSquareIndex() {
        return squareIndex;
    }

    public long getLayerX() {
        return layerX;
    }

    public long getLayerY() {
        return layerY;
    }

    public void calculatePoints( int[][] points ) {
        SquareCalculatorFactory.createCalculator( points, 0, 0, SQUARE_SIDE_SIZE, leftRe, topIm,
                width, null ).calculatePoints();
    }

    @Override
    public int compareTo( Square other ) {
        // Order first by IM, then by RE.
        if ( this.topIm != other.topIm ) {
            return -1 * ( Double.compare( this.topIm, other.topIm ) );
        } else {
            return Double.compare( this.leftRe, other.leftRe );
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits( leftRe );
        result = prime * result + ( int ) ( temp ^ ( temp >>> 32 ) );
        temp = Double.doubleToLongBits( topIm );
        result = prime * result + ( int ) ( temp ^ ( temp >>> 32 ) );
        temp = Double.doubleToLongBits( width );
        result = prime * result + ( int ) ( temp ^ ( temp >>> 32 ) );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        Square other = ( Square ) obj;
        if ( Double.doubleToLongBits( leftRe ) != Double.doubleToLongBits( other.leftRe ) ) return false;
        if ( Double.doubleToLongBits( topIm ) != Double.doubleToLongBits( other.topIm ) ) return false;
        if ( Double.doubleToLongBits( width ) != Double.doubleToLongBits( other.width ) ) return false;
        return true;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations( int iterations ) {
        this.iterations = iterations;
    }

    @Override
    public String toString() {
        return "Square [layerIndex=" + layer.getLayerIndex() + ", leftRe=" + leftRe + ", topIm="
                + topIm + ", iterations=" + iterations + ", layerX=" + layerX + ", layerY="
                + layerY + ", squareIndex=" + squareIndex + "]";
    }
}
