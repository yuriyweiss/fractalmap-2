package org.fractal.map.calc.square;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.square.point.AbstractPointCalculationTask;
import org.fractal.map.calc.square.point.PointCalcFinishedListener;
import org.fractal.map.calc.square.point.PointCalculationTaskFactory;
import org.fractal.map.context.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.fractal.map.calc.Constants.POINT_NEEDS_CALC;

public abstract class AbstractSquareCalculator implements PointCalcFinishedListener {

    private static final Logger logger = LogManager.getLogger();

    private AbstractSquareCalculator parent = null;

    protected final int[][] originalPoints;

    protected final int sideSize;

    // indexes of topLeft corner in original grandparent's points array
    protected final int leftIndex;
    protected final int topIndex;

    // real left top coords of this exact square
    protected final double leftRe;
    protected final double topIm;

    // real width of this square
    protected final double width;

    protected int pointsProcessed = 0;
    protected int pointsCount;
    protected int calcPoints = 0;

    protected ExecutorService executor;
    protected long startTimeMillis;

    protected abstract void performCalculation();

    public AbstractSquareCalculator( int[][] originalPoints, int leftIndex, int topIndex,
            int sideSize, double leftRe, double topIm, double width, AbstractSquareCalculator parent ) {
        this.parent = parent;

        this.originalPoints = originalPoints;
        this.sideSize = sideSize;
        this.leftIndex = leftIndex;
        this.topIndex = topIndex;

        this.leftRe = leftRe;
        this.topIm = topIm;

        this.width = width;
    }

    public void calculatePoints() {
        startTimeMillis = System.currentTimeMillis();

        performCalculation();

        updateGlobalCounters();

        if ( isTopParent() ) {
            logger.trace( "square processed" );
        }
    }

    private boolean isTopParent() {
        return parent == null;
    }

    public void onPointCalcFinished() {
        synchronized ( this ) {
            pointsProcessed++;
            if ( pointsProcessed == pointsCount ) {
                calcPoints = pointsCount;
                updateParentCalcPoints( pointsCount );
                executor.shutdown();
                notifyAll();
            }
        }
    }

    private void updateParentCalcPoints( int pointsCount ) {
        if ( parent != null ) {
            parent.addCalcPoints( pointsCount );
            // grandparent chain update too
            parent.updateParentCalcPoints( pointsCount );
        }
    }

    private void addCalcPoints( int pointsCount ) {
        calcPoints += pointsCount;
    }

    private void updateGlobalCounters() {
        if ( isTopParent() ) {
            long calculationTimeMillis = System.currentTimeMillis() - startTimeMillis;
            Context.getBaseCounters().getTotalCalculationTimeMillis().addAndGet(
                    calculationTimeMillis );
            Context.getBaseCounters().getTotalProcessedSquares().incrementAndGet();
            Context.getBaseCounters().getPeriodicProcessedSquares().incrementAndGet();
            Context.getBaseCounters().getTotalCalculatedPoints().addAndGet( calcPoints );
        }
    }

    protected void addPointTask( List<AbstractPointCalculationTask> tasksToExecute, int x, int y,
            double pointRe, double pointIm ) {
        if ( originalPoints[y][x] == POINT_NEEDS_CALC ) {
            AbstractPointCalculationTask pointCalcTask =
                    PointCalculationTaskFactory.createTask( originalPoints, x, y, pointRe, pointIm, this );
            tasksToExecute.add( pointCalcTask );
        }
    }
}
