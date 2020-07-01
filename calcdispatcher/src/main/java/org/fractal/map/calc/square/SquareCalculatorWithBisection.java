package org.fractal.map.calc.square;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.fractal.map.calc.square.point.AbstractPointCalculationTask;
import org.fractal.map.conf.Configuration;

public class SquareCalculatorWithBisection extends AbstractSquareCalculator {

    public SquareCalculatorWithBisection( int[][] originalPoints, int leftIndex, int topIndex,
            int sideSize, double leftRe, double topIm, double width, AbstractSquareCalculator parent ) {
        super( originalPoints, leftIndex, topIndex, sideSize, leftRe, topIm, width, parent );
    }

    @Override
    protected void performCalculation() {
        calculateBorder();

        Integer commonIterations = getBorderCommonIterations();
        if ( commonIterations == null ) {
            calculateChildren();
        } else {
            fillBodyPoints( commonIterations.intValue() );
        }
    }

    private void calculateBorder() {
        executor = Executors.newFixedThreadPool( Configuration.getCalcSquareThreadsCount() );

        List<AbstractPointCalculationTask> tasksToExecute = new ArrayList<>();
        fillPointCalculationTasks( tasksToExecute );
        pointsCount = tasksToExecute.size();

        for ( AbstractPointCalculationTask task : tasksToExecute ) {
            executor.execute( task );
        }

        synchronized ( this ) {
            while ( pointsCount != pointsProcessed ) {
                try {
                    wait();
                } catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                    executor.shutdown();
                }
            }
        }
    }

    private void fillPointCalculationTasks( List<AbstractPointCalculationTask> tasksToExecute ) {
        double cellWidth = width / sideSize;
        // Points from top to bottom.
        double pointIm;
        double pointReLeft = leftRe + cellWidth / 2;
        double pointReRight = leftRe + width - cellWidth / 2;
        for ( int y = 0; y < sideSize; y++ ) {
            pointIm = topIm - cellWidth * y - cellWidth / 2;
            addPointTask( tasksToExecute, leftIndex, y + topIndex, pointReLeft, pointIm );
            addPointTask( tasksToExecute, leftIndex + sideSize - 1, y + topIndex, pointReRight,
                    pointIm );
        }
        // Points from left to right.
        double pointRe;
        double pointImTop = topIm - cellWidth / 2;
        double pointImBottom = topIm - width + cellWidth / 2;
        for ( int x = 1; x < sideSize - 1; x++ ) {
            pointRe = leftRe + x * cellWidth + cellWidth / 2;
            addPointTask( tasksToExecute, x + leftIndex, topIndex, pointRe, pointImTop );
            addPointTask( tasksToExecute, x + leftIndex, topIndex + sideSize - 1, pointRe,
                    pointImBottom );
        }
    }

    private Integer getBorderCommonIterations() {
        int result = originalPoints[topIndex][leftIndex];
        for ( int y = 0; y < sideSize; y++ ) {
            if ( originalPoints[y + topIndex][leftIndex] != result ) return null;
            if ( originalPoints[y + topIndex][leftIndex + sideSize - 1] != result ) return null;
        }
        for ( int x = 1; x < sideSize - 1; x++ ) {
            if ( originalPoints[topIndex][x + leftIndex] != result ) return null;
            if ( originalPoints[topIndex + sideSize - 1][x + leftIndex] != result ) return null;
        }
        return result;
    }

    private void calculateChildren() {
        // Need calculate real new square topLeft coordinates,
        // real new square width, new side size and left and top indexes of
        // leftTop point in original points array.
        int childSideSize = sideSize / 2;
        double childWidth = width / 2.0;
        SquareCalculatorFactory.createCalculator( originalPoints, leftIndex, topIndex,
                childSideSize, leftRe, topIm, childWidth, this ).calculatePoints();
        SquareCalculatorFactory.createCalculator( originalPoints, leftIndex + childSideSize,
                topIndex, childSideSize, leftRe + childWidth, topIm, childWidth, this ).calculatePoints();
        SquareCalculatorFactory.createCalculator( originalPoints, leftIndex,
                topIndex + childSideSize, childSideSize, leftRe, topIm - childWidth, childWidth,
                this ).calculatePoints();
        SquareCalculatorFactory.createCalculator( originalPoints, leftIndex + childSideSize,
                topIndex + childSideSize, childSideSize, leftRe + childWidth, topIm - childWidth,
                childWidth, this ).calculatePoints();
    }

    private void fillBodyPoints( int commonIterations ) {
        for ( int y = topIndex + 1; y < topIndex + sideSize - 1; y++ ) {
            for ( int x = leftIndex + 1; x < leftIndex + sideSize - 1; x++ ) {
                originalPoints[y][x] = commonIterations;
            }
        }
    }
}
