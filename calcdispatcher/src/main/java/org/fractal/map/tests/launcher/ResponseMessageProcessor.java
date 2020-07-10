package org.fractal.map.tests.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.context.Context;
import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.AreaSquarePartitionRequest;
import org.fractal.map.message.request.SquareRequest;
import org.fractal.map.message.response.AreaSquarePartitionResponse;
import org.fractal.map.message.response.PointCoordsResponse;
import org.fractal.map.message.response.SquareInfo;
import org.fractal.map.transceiver.MessageProcessor;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.transceiver.client.TransceiverClient;
import org.fractal.map.waiterscache.WaitersCache;

import java.util.List;
import java.util.UUID;

public class ResponseMessageProcessor implements MessageProcessor {

    private static final Logger logger = LogManager.getLogger();

    private final TransceiverClient transceiverClient;
    private final WaitersCache waiters;

    public ResponseMessageProcessor( TransceiverClient transceiverClient, WaitersCache waiters ) {
        this.transceiverClient = transceiverClient;
        this.waiters = waiters;
    }

    @Override
    public void onMessageDecoded( UUID clientUUID, Transportable message ) {
        Context.getBaseCounters().getPeriodicTransceiverMessages().getAndIncrement();
        Context.getBaseCounters().getTotalTransceiverMessages().getAndIncrement();

        ServletMessage response = ( ServletMessage ) message;
        waiters.onResponseArrived( response.getRequestUUID(), response );
        logger.debug( "response arrived: {}", response );
        int classId = response.getClassId();
        if ( classId == MessagesRegistrator.RESPONSE_POINT_COORDS ) {
            processPointCoordsResponse( ( PointCoordsResponse ) response );
        } else if ( classId == MessagesRegistrator.RESPONSE_AREA_SQUARE_PARTITION ) {
            processAreaSquarePartitionResponse( ( AreaSquarePartitionResponse ) response );
        }
    }

    private void processPointCoordsResponse( PointCoordsResponse response ) {
        AreaSquarePartitionRequest request =
                new AreaSquarePartitionRequest( UUID.randomUUID(), 5, response.getRe(), response.getIm(), 1024, 768 );
        logger.debug( "sending request: {}", request );
        waiters.put( request.getRequestUUID(), new DummyWaiter() );
        transceiverClient.send( request );
    }

    private void processAreaSquarePartitionResponse( AreaSquarePartitionResponse response ) {
        List<SquareInfo> squares = response.getSquares();
        for ( SquareInfo squareInfo : squares ) {
            SquareRequest request =
                    new SquareRequest( UUID.randomUUID(), 5, squareInfo.getLeftRe(), squareInfo.getTopIm() );
            logger.debug( "sending request: {}", request );
            waiters.put( request.getRequestUUID(), new DummyWaiter() );
            transceiverClient.send( request );
        }
    }
}
