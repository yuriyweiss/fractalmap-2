package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractLongKpi;

public class PointsPerFiveSecondsKpi extends AbstractLongKpi {

    @Override
    public void updateValue() {
        setValue( Context.getBaseCounters().getPeriodicCalculatedPoints().get() );
        Context.getBaseCounters().getPeriodicCalculatedPoints().set( 0L );
    }
}
