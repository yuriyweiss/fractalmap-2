package org.fractal.map.context;

import java.util.concurrent.atomic.AtomicLong;

public class BaseCountersContext {

    private long squaresToCalculate;

    private AtomicLong totalCalculatedPoints = new AtomicLong( 0L );
    private AtomicLong totalCalculatedSetPoints = new AtomicLong( 0L );
    private AtomicLong totalIterationsBySetPoints = new AtomicLong( 0L );
    private AtomicLong totalProcessedSquares = new AtomicLong( 0L );
    private AtomicLong totalGuessedSquares = new AtomicLong( 0L );
    private AtomicLong totalCalculationTimeMillis = new AtomicLong( 0L );
    private AtomicLong totalTransceiverMessages = new AtomicLong( 0L );

    private AtomicLong periodicCalculatedPoints = new AtomicLong( 0L );
    private AtomicLong periodicProcessedSquares = new AtomicLong( 0L );
    private AtomicLong periodicTransceiverMessages = new AtomicLong( 0L );

    public long getSquaresToCalculate() {
        return squaresToCalculate;
    }

    public void setSquaresToCalculate( long squaresToCalculate ) {
        this.squaresToCalculate = squaresToCalculate;
    }

    public AtomicLong getTotalCalculatedPoints() {
        return totalCalculatedPoints;
    }

    public AtomicLong getTotalProcessedSquares() {
        return totalProcessedSquares;
    }

    public AtomicLong getTotalGuessedSquares() {
        return totalGuessedSquares;
    }

    public AtomicLong getTotalCalculationTimeMillis() {
        return totalCalculationTimeMillis;
    }

    public AtomicLong getPeriodicCalculatedPoints() {
        return periodicCalculatedPoints;
    }

    public AtomicLong getPeriodicProcessedSquares() {
        return periodicProcessedSquares;
    }

    public AtomicLong getTotalCalculatedSetPoints() {
        return totalCalculatedSetPoints;
    }

    public AtomicLong getTotalIterationsBySetPoints() {
        return totalIterationsBySetPoints;
    }

    public AtomicLong getTotalTransceiverMessages() {
        return totalTransceiverMessages;
    }

    public AtomicLong getPeriodicTransceiverMessages() {
        return periodicTransceiverMessages;
    }
}
