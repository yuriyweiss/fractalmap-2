package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractDoubleKpi;

public class AvgSquareCalcTimeKpi extends AbstractDoubleKpi {

    @Override
    public void updateValue() {
        long totalProcessedSquares = Context.getBaseCounters().getTotalProcessedSquares().get();
        long totalGuessedSquares = Context.getBaseCounters().getTotalGuessedSquares().get();
        long totalCalculationTimeMillis =
                Context.getBaseCounters().getTotalCalculationTimeMillis().get();
        if ( totalProcessedSquares == 0 ) {
            setValue( -1 );
        } else {
            setValue( totalCalculationTimeMillis
                    / ( double ) ( totalProcessedSquares - totalGuessedSquares ) );
        }
    }
}
