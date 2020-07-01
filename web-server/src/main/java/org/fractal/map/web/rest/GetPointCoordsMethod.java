package org.fractal.map.web.rest;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.PointCoordsRequest;
import org.fractal.map.message.response.PointCoordsResponse;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.web.ServletError;
import org.fractal.map.web.response.AbstractInfo;
import org.fractal.map.web.response.PointCoordsInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@Component
public class GetPointCoordsMethod extends AbstractResponseWaitingMethod {

    @Override
    protected ServletMessage createMessage( Map<String, Object> requestParams, HttpSession httpSession ) {
        Integer layerIndex = ( Integer ) httpSession.getAttribute( Names.CURRENT_LAYER_INDEX );
        Double re = ( Double ) httpSession.getAttribute( Names.CENTER_RE );
        Double im = ( Double ) httpSession.getAttribute( Names.CENTER_IM );
        int shiftX = ( Integer ) requestParams.get( Names.SHIFT_X );
        int shiftY = ( Integer ) requestParams.get( Names.SHIFT_Y );
        return new PointCoordsRequest( UUID.randomUUID(), layerIndex, re, im, shiftX, shiftY );
    }

    @Override
    protected AbstractInfo writeErrorResponse( ServletError error ) {
        PointCoordsInfo pointCoordsInfo = new PointCoordsInfo();
        pointCoordsInfo.setWasError( true );
        pointCoordsInfo.setError( error );
        return pointCoordsInfo;
    }

    @Override
    protected AbstractInfo writeReplyMessageToResponse( Transportable replyMessage ) {
        PointCoordsResponse message = ( PointCoordsResponse ) replyMessage;
        return new PointCoordsInfo( message.getRe(), message.getIm() );
    }
}
