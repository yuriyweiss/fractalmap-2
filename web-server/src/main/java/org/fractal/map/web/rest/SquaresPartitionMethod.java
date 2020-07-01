package org.fractal.map.web.rest;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.AreaSquarePartitionRequest;
import org.fractal.map.message.response.AreaSquarePartitionResponse;
import org.fractal.map.message.response.SquareInfo;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.web.ServletError;
import org.fractal.map.web.response.AbstractInfo;
import org.fractal.map.web.response.SquarePartitionInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@Component
public class SquaresPartitionMethod extends AbstractResponseWaitingMethod {

    @Override
    protected ServletMessage createMessage( Map<String, Object> requestParams, HttpSession httpSession ) {
        Double centerRe = ( Double ) httpSession.getAttribute( Names.CENTER_RE );
        Double centerIm = ( Double ) httpSession.getAttribute( Names.CENTER_IM );
        Integer currentLayerIndex = ( Integer ) httpSession.getAttribute( Names.CURRENT_LAYER_INDEX );
        int areaWidth = ( Integer ) requestParams.get( Names.AREA_WIDTH );
        int areaHeight = ( Integer ) requestParams.get( Names.AREA_HEIGHT );
        return new AreaSquarePartitionRequest(
                UUID.randomUUID(), currentLayerIndex, centerRe, centerIm, areaWidth, areaHeight );
    }

    @Override
    protected AbstractInfo writeErrorResponse( ServletError error ) {
        SquarePartitionInfo squarePartitionInfo = new SquarePartitionInfo();
        squarePartitionInfo.setWasError( true );
        squarePartitionInfo.setError( error );
        return squarePartitionInfo;
    }

    @Override
    protected AbstractInfo writeReplyMessageToResponse( Transportable replyMessage ) {
        AreaSquarePartitionResponse message = ( AreaSquarePartitionResponse ) replyMessage;
        SquarePartitionInfo squarePartitionInfo = new SquarePartitionInfo();
        for ( SquareInfo squareInfo : message.getSquares() ) {
            squarePartitionInfo.addSquare( squareInfo.getLeftRe(), squareInfo.getTopIm(),
                    squareInfo.getCanvasLeftX(), squareInfo.getCanvasTopY() );
        }
        return squarePartitionInfo;
    }
}
