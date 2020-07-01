package org.fractal.map.context;

import org.fractal.map.calc.square.SquaresPartition;

public class Context {

    private static final BaseCountersContext baseCounters = new BaseCountersContext();
    private static SquaresPartition squaresPartition;

    public static BaseCountersContext getBaseCounters() {
        return baseCounters;
    }

    public static void setSquaresPartition( SquaresPartition squaresPartition ) {
        Context.squaresPartition = squaresPartition;
    }

    public static SquaresPartition getSquaresPartition() {
        return squaresPartition;
    }
}
