package org.fractal.map.model;

public class Point {

    private final double re;
    private final double im;

    public Point( double re, double im ) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }
}
