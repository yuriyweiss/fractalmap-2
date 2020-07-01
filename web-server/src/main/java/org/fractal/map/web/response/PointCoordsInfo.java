package org.fractal.map.web.response;

public class PointCoordsInfo extends AbstractInfo {

    private final double re;
    private final double im;

    public PointCoordsInfo() {
        this.re = 0;
        this.im = 0;
    }

    public PointCoordsInfo( double re, double im ) {
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
