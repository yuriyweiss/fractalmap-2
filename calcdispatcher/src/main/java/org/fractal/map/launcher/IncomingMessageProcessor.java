package org.fractal.map.launcher;

import org.fractal.map.context.Context;
import org.fractal.map.message.request.Request;
import org.fractal.map.transceiver.MessageProcessor;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.transceiver.server.ServerMessage;
import org.fractal.map.transceiver.server.TransceiverServer;

import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

public class IncomingMessageProcessor implements MessageProcessor {

    private final ThreadPoolExecutor calcExecutor;
    private final ThreadPoolExecutor squareExecutor;
    private final TransceiverServer transceiverServer;

    public IncomingMessageProcessor( ThreadPoolExecutor calcExecutor,
            ThreadPoolExecutor squareExecutor, TransceiverServer transceiverServer ) {
        this.calcExecutor = calcExecutor;
        this.squareExecutor = squareExecutor;
        this.transceiverServer = transceiverServer;
    }

    @Override
    public void onMessageDecoded( UUID clientUUID, Transportable message ) {
        Context.getBaseCounters().getPeriodicTransceiverMessages().getAndIncrement();
        Context.getBaseCounters().getTotalTransceiverMessages().getAndIncrement();

        ServerMessage request = new ServerMessage( clientUUID, message );
        if ( !( message instanceof Request ) ) return;

        MessageProcessingTask messageProcessingTask =
                new MessageProcessingTask( request, transceiverServer );
        if ( ( ( Request ) message ).isCalcRequest() ) {
            calcExecutor.execute( messageProcessingTask );
        } else {
            squareExecutor.execute( messageProcessingTask );
        }
    }
}
