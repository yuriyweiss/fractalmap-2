package org.fractal.map.web.rest;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.FindRootRequest;
import org.fractal.map.message.response.FindRootResponse;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.web.ServletError;
import org.fractal.map.web.response.AbstractInfo;
import org.fractal.map.web.response.FindRootInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@Component
public class FindRootMethod extends AbstractResponseWaitingMethod {

    @Override
    protected ServletMessage createMessage( Map<String, Object> requestParams, HttpSession httpSession ) {
        Integer layerIndex = ( Integer ) httpSession.getAttribute( Names.CURRENT_LAYER_INDEX );
        double leftRe = ( Double ) requestParams.get( Names.LEFT_RE );
        double topIm = ( Double ) requestParams.get( Names.TOP_IM );
        double rightRe = ( Double ) requestParams.get( Names.RIGHT_RE );
        double bottomIm = ( Double ) requestParams.get( Names.BOTTOM_IM );
        return new FindRootRequest( UUID.randomUUID(), layerIndex, leftRe, topIm, rightRe, bottomIm );
    }

    @Override
    protected AbstractInfo writeErrorResponse( ServletError error ) {
        FindRootInfo findRootInfo = new FindRootInfo();
        findRootInfo.setWasError( true );
        findRootInfo.setError( error );
        return findRootInfo;
    }

    @Override
    protected AbstractInfo writeReplyMessageToResponse( Transportable replyMessage ) {
        FindRootResponse message = ( FindRootResponse ) replyMessage;
        return new FindRootInfo( message.getPolynomialDegree() );
    }
}
