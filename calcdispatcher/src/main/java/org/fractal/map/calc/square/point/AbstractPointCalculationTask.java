package org.fractal.map.calc.square.point;

import org.fractal.map.context.Context;

import static org.fractal.map.calc.Constants.POINT_IN_SET;

public abstract class AbstractPointCalculationTask implements Runnable {

    protected final int[][] originalPoints;
    protected final int x;
    protected final int y;
    protected final double re;
    protected final double im;

    protected boolean pointBelongsToSet = false;
    protected int iterations = 0;

    protected final PointCalcFinishedListener calcFinishedListener;

    protected abstract void calculatePoint();

    public AbstractPointCalculationTask( int[][] originalPoints, int x, int y, double re, double im,
            PointCalcFinishedListener calcFinishedListener ) {
        this.originalPoints = originalPoints;
        this.x = x;
        this.y = y;
        this.re = re;
        this.im = im;
        this.calcFinishedListener = calcFinishedListener;
    }

    @Override
    public void run() {
        calculatePoint();
        originalPoints[y][x] = pointBelongsToSet ? POINT_IN_SET : iterations;

        Context.getBaseCounters().getPeriodicCalculatedPoints().getAndIncrement();
        if ( pointBelongsToSet ) {
            Context.getBaseCounters().getTotalCalculatedSetPoints().incrementAndGet();
            Context.getBaseCounters().getTotalIterationsBySetPoints().addAndGet( iterations );
        }

        if ( calcFinishedListener != null ) {
            calcFinishedListener.onPointCalcFinished();
        }
    }
}
