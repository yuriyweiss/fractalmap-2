package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractDoubleKpi;

public class TotalCalculationTimeInSecondsKpi extends AbstractDoubleKpi {

    @Override
    public void updateValue() {
        setValue( Context.getBaseCounters().getTotalCalculationTimeMillis().get() / 1000.0 );
    }
}
