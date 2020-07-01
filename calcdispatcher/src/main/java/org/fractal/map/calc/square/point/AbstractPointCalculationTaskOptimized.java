package org.fractal.map.calc.square.point;

import org.fractal.map.conf.Configuration;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPointCalculationTaskOptimized extends AbstractPointCalculationTask {

    protected int maxIterations;
    protected int bufferSize;
    protected int bufferingStep;

    private int nextBufferingStart;
    private int nextBufferCheck;
    private boolean bufferingActive = false;
    private Map<Integer, TruncatedComplex> buffer = new HashMap<>();

    protected abstract int getNextBufferingStartIteration();

    public AbstractPointCalculationTaskOptimized( int[][] originalPoints, int x, int y, double re, double im,
            PointCalcFinishedListener calcFinishedListener ) {
        super( originalPoints, x, y, re, im, calcFinishedListener );

        this.maxIterations = Configuration.getIterationsCount();
        this.bufferSize = Configuration.getPointOptimizationBufferSize();
        this.bufferingStep = 1;
    }

    private void preparePeriodChecking() {
        nextBufferingStart = getNextBufferingStartIteration();
        nextBufferCheck = nextBufferingStart + bufferSize + 1;
    }

    @Override
    protected void calculatePoint() {
        preparePeriodChecking();

        double prevRe = 0;
        double prevIm = 0;
        double currRe = 0;
        double currIm = 0;
        int iterCount = 0;
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
            if ( iterCount == nextBufferingStart ) {
                buffer.clear();
                bufferingActive = true;
                bufferingStep++;
                nextBufferingStart = getNextBufferingStartIteration();
            } else if ( bufferingActive && iterCount == nextBufferCheck ) {
                if ( pointIsPeriodic( currRe, currIm ) ) {
                    pointBelongsToSet = true;
                    break;
                }
                bufferingActive = false;
                nextBufferCheck = nextBufferingStart + bufferSize + 1;
            }
            if ( bufferingActive ) {
                putToBuffer( currRe, currIm );
            }
        }
        if ( !pointBelongsToSet ) {
            pointBelongsToSet = iterCount == maxIterations;
        }
        iterations = iterCount;
    }

    private void putToBuffer( double currRe, double currIm ) {
        TruncatedComplex truncated = new TruncatedComplex( currRe, currIm );
        buffer.put( truncated.hashCode(), new TruncatedComplex( currRe, currIm ) );
    }

    private boolean pointIsPeriodic( double currRe, double currIm ) {
        TruncatedComplex truncated = new TruncatedComplex( currRe, currIm );
        TruncatedComplex candidate = buffer.get( truncated.hashCode() );
        return truncated.equals( candidate );
    }
}
