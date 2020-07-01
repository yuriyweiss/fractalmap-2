package org.fractal.map.calc.square.point;

import org.fractal.map.conf.Configuration;

public class PointCalculationTaskOptimizedGeometric extends AbstractPointCalculationTaskOptimized {

    private int firstInterval;
    private double intervalMagnifier;

    public PointCalculationTaskOptimizedGeometric( int[][] originalPoints, int x, int y, double re, double im,
			PointCalcFinishedListener calcFinishedListener ) {
        super( originalPoints, x, y, re, im, calcFinishedListener );
        this.firstInterval = Configuration.getPointOptimizationGeomFirstInterval();
        this.intervalMagnifier = Configuration.getPointOptimizationGeomIntervalMagnifier();
    }

    @Override
    protected int getNextBufferingStartIteration() {
        int result = bufferSize * ( bufferingStep - 1 );
        result +=
                ( int ) Math.round( ( firstInterval * ( Math.pow( intervalMagnifier, bufferingStep ) - 1 ) )
                        / ( intervalMagnifier - 1 ) );
        return result;
    }
}
