package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractLongKpi;

public class SquaresPerFiveSecondsKpi extends AbstractLongKpi {

    @Override
    public void updateValue() {
        setValue( Context.getBaseCounters().getPeriodicProcessedSquares().get() );
        Context.getBaseCounters().getPeriodicProcessedSquares().set( 0L );
    }
}
