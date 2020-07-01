package org.fractal.map.calc.square;

import org.fractal.map.conf.Configuration;

public class SquareCalculatorFactory {

    public static AbstractSquareCalculator createCalculator( int[][] originalPoints, int leftIndex,
            int topIndex, int sideSize, double leftRe, double topIm, double width,
            AbstractSquareCalculator parent ) {
        if ( Configuration.isSquareOptimizationActive() ) {
            if ( sideSize > Configuration.getSquareOptimizationSideSizeLimit() ) {
                return new SquareCalculatorWithBisection( originalPoints, leftIndex, topIndex, sideSize, leftRe, topIm,
                        width, parent );
            } else {
                return new SquareCalculatorSimple( originalPoints, leftIndex, topIndex, sideSize, leftRe, topIm, width,
                        parent );
            }
        } else {
            return new SquareCalculatorSimple( originalPoints, leftIndex, topIndex, sideSize, leftRe, topIm, width,
                    parent );
        }
    }
}
