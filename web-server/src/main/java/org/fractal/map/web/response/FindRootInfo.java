package org.fractal.map.web.response;

public class FindRootInfo extends AbstractInfo {

    private final int polynomialDegree;

    public FindRootInfo() {
        this.polynomialDegree = -1;
    }

    public FindRootInfo( int polynomialDegree ) {
        this.polynomialDegree = polynomialDegree;
    }

    public int getPolynomialDegree() {
        return polynomialDegree;
    }
}
