package org.fractal.map.web.transceiver;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.waiterscache.ResponseWaiter;
import org.fractal.map.waiterscache.WaitersCache;

import java.util.UUID;

public class RequestsCache {

    private static final WaitersCache waiters = new WaitersCache( 15 );

    private RequestsCache() {
    }

    public static void registerWaiter( UUID requestUUID, ResponseWaiter waiter ) {
        waiters.put( requestUUID, waiter );
    }

    public static void onResponseArrived( UUID requestUUID, ServletMessage response ) {
        waiters.onResponseArrived( requestUUID, response );
    }
}
