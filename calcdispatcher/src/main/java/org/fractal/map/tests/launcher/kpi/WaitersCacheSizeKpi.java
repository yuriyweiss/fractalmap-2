package org.fractal.map.tests.launcher.kpi;

import org.apache.logging.log4j.Logger;
import org.fractal.map.monitor.AbstractLongKpi;
import org.fractal.map.monitor.AdditionalKpi;
import org.fractal.map.waiterscache.WaitersCache;

public class WaitersCacheSizeKpi extends AbstractLongKpi implements AdditionalKpi {

    private final WaitersCache waitersCache;

    public WaitersCacheSizeKpi( WaitersCache waitersCache ) {
        this.waitersCache = waitersCache;
    }

    @Override
    public void updateValue() {
        if ( waitersCache != null ) {
            setValue( waitersCache.getSize() );
        }
    }

    @Override
    public void logValue( Logger logger ) {
        logger.info( "POOL SIZE waiters: {}", getValue() );
    }
}
