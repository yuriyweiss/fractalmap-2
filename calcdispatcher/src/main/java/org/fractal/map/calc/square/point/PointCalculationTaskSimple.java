package org.fractal.map.calc.square.point;

import org.fractal.map.conf.Configuration;

public class PointCalculationTaskSimple extends AbstractPointCalculationTask {

    public PointCalculationTaskSimple( int[][] originalPoints, int x, int y, double re, double im,
			PointCalcFinishedListener calcFinishedListener ) {
        super( originalPoints, x, y, re, im, calcFinishedListener );
    }

    @Override
    protected void calculatePoint() {
        double prevRe = 0;
        double prevIm = 0;
        double currRe = 0;
        double currIm = 0;
        int iterCount = 0;
        int maxIterations = Configuration.getIterationsCount();
        while ( iterCount < maxIterations ) {
            currRe = prevRe * prevRe - prevIm * prevIm + re;
            currIm = 2 * prevRe * prevIm + im;
            double norm = currRe * currRe + currIm * currIm;
            prevRe = currRe;
            prevIm = currIm;
            iterCount++;
            if ( norm > 10 ) {
                break;
            }
        }
        pointBelongsToSet = iterCount == maxIterations;
        iterations = iterCount;
    }
}
