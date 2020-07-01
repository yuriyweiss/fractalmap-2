package org.fractal.map.calc.square.point;

import org.fractal.map.conf.Configuration;

public class PointCalculationTaskOptimizedUniform extends AbstractPointCalculationTaskOptimized {

    private int checkCount;
    private int bufferingDelta;

    public PointCalculationTaskOptimizedUniform( int[][] originalPoints, int x, int y, double re, double im,
            PointCalcFinishedListener calcFinishedListener ) {
        super( originalPoints, x, y, re, im, calcFinishedListener );
        this.checkCount = Configuration.getPointOptimizationUniformCheckCount();
        this.bufferingDelta = ( maxIterations - ( bufferSize * checkCount ) ) / ( checkCount + 1 );
    }

    @Override
    protected int getNextBufferingStartIteration() {
        return ( bufferingDelta + bufferSize ) * bufferingStep - bufferSize;
    }
}
