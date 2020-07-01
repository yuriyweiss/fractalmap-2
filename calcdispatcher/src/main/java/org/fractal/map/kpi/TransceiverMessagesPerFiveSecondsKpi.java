package org.fractal.map.kpi;

import org.fractal.map.context.Context;
import org.fractal.map.monitor.AbstractLongKpi;

public class TransceiverMessagesPerFiveSecondsKpi extends AbstractLongKpi {

    @Override
    public void updateValue() {
        setValue( Context.getBaseCounters().getPeriodicTransceiverMessages().get() );
        Context.getBaseCounters().getPeriodicTransceiverMessages().set( 0L );
    }
}
