package org.fractal.map.web.rest;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.web.ServletError;
import org.fractal.map.web.response.AbstractInfo;
import org.fractal.map.web.transceiver.RequestsCache;
import org.fractal.map.web.transceiver.WebServerTransceiverClient;

import javax.servlet.http.HttpSession;
import java.util.Map;

public abstract class AbstractResponseWaitingMethod {

    protected abstract ServletMessage createMessage( Map<String, Object> requestParams, HttpSession httpSession );

    protected abstract AbstractInfo writeErrorResponse( ServletError error );

    protected abstract AbstractInfo writeReplyMessageToResponse( Transportable replyMessage );

    public AbstractInfo processGetRequest( Map<String, Object> requestParams, HttpSession httpSession,
            WebServerTransceiverClient webServerTransceiverClient ) {
        if ( httpSession == null ) {
            return writeErrorResponse( ServletError.SESSION_NOT_EXISTS );
        }

        ServletMessage message = createMessage( requestParams, httpSession );
        MethodResponseWaiter responseWaiter = new MethodResponseWaiter();
        RequestsCache.registerWaiter( message.getRequestUUID(), responseWaiter );
        webServerTransceiverClient.send( message );

        boolean interrupted = false;
        synchronized ( responseWaiter ) {
            while ( !responseWaiter.isMessageProcessed() ) {
                try {
                    responseWaiter.wait( 5000L );
                } catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                    interrupted = true;
                    break;
                }
            }
        }

        return writeResponseAfterWaiting( responseWaiter, interrupted );
    }

    protected AbstractInfo writeResponseAfterWaiting( MethodResponseWaiter responseWaiter, boolean interrupted ) {
        if ( interrupted ) {
            return writeErrorResponse( ServletError.MESSAGE_WAITING_INTERRUPTED );
        } else if ( responseWaiter.isMessageTimeout() ) {
            return writeErrorResponse( ServletError.MESSAGE_TIMEOUT );
        } else if ( responseWaiter.getReplyMessage() != null ) {
            return writeReplyMessageToResponse( responseWaiter.getReplyMessage() );
        } else {
            return writeErrorResponse( ServletError.UNKNOWN_ERROR );
        }
    }
}
