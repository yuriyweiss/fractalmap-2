package org.fractal.map.web.rest;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.waiterscache.ResponseWaiter;

public class MethodResponseWaiter implements ResponseWaiter {

    private Transportable replyMessage = null;
    private boolean messageTimeout = false;

    @Override
    public void setResponse( ServletMessage replyMessage ) {
        this.replyMessage = replyMessage;
    }

    @Override
    public void setResponseTimeout() {
        messageTimeout = true;
    }

    public boolean isMessageProcessed() {
        return ( replyMessage != null ) || messageTimeout;
    }

    public Transportable getReplyMessage() {
        return replyMessage;
    }

    public boolean isMessageTimeout() {
        return messageTimeout;
    }
}
