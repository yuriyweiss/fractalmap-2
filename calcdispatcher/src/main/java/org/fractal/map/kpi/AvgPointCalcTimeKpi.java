package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractDoubleKpi;

public class AvgPointCalcTimeKpi extends AbstractDoubleKpi {

    @Override
    public void updateValue() {
        long totalCalculatedPoints = Context.getBaseCounters().getTotalCalculatedPoints().get();
        long totalCalculationTimeMillis =
                Context.getBaseCounters().getTotalCalculationTimeMillis().get();
        if ( totalCalculatedPoints == 0 ) {
            setValue( -1 );
        } else {
            setValue( totalCalculationTimeMillis / ( double ) totalCalculatedPoints );
        }
    }
}
