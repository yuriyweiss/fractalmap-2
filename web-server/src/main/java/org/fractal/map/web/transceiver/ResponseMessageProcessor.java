package org.fractal.map.web.transceiver;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.transceiver.MessageProcessor;
import org.fractal.map.transceiver.Transportable;

import java.util.UUID;

public class ResponseMessageProcessor implements MessageProcessor {

    @Override
    public void onMessageDecoded( UUID requestUUID, Transportable message ) {
        ServletMessage response = ( ServletMessage ) message;
        RequestsCache.onResponseArrived( response.getRequestUUID(), response );
    }
}
