package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractLongKpi;

public class TotalTransceiverMessagesKpi extends AbstractLongKpi {

    @Override
    public void updateValue() {
        setValue( Context.getBaseCounters().getTotalTransceiverMessages().get() );
    }
}
