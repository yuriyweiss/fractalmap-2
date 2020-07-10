package org.fractal.map.calc.square.point;

import org.fractal.map.conf.Configuration;
import org.fractal.map.exception.FatalFractalMapException;

public class PointCalculationTaskFactory {

    private PointCalculationTaskFactory() {
    }

    public static AbstractPointCalculationTask createTask( int[][] originalPoints, int x, int y, double re,
            double im, PointCalcFinishedListener calcFinishedListener ) {
        if ( Configuration.isPointOptimizationActive() ) {
            if ( "uniform".equals( Configuration.getPointOptimizationType() ) ) {
                return new PointCalculationTaskOptimizedUniform( originalPoints, x, y, re, im, calcFinishedListener );
            } else if ( "geometric".equals( Configuration.getPointOptimizationType() ) ) {
                return new PointCalculationTaskOptimizedGeometric( originalPoints, x, y, re, im, calcFinishedListener );
            } else {
                throw new FatalFractalMapException( "ERROR config parameter point.optimization.type UNKNOWN value ["
                        + Configuration.getPointOptimizationType() + "]" );
            }
        } else {
            return new PointCalculationTaskSimple( originalPoints, x, y, re, im, calcFinishedListener );
        }
    }
}
