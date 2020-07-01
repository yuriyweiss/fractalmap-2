package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractLongKpi;

public class TotalSquaresToCalculateKpi extends AbstractLongKpi {

    @Override
    public void updateValue() {
        setValue( Context.getBaseCounters().getSquaresToCalculate() );
    }
}
