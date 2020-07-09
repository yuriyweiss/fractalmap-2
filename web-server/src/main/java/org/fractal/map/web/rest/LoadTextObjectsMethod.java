package org.fractal.map.web.rest;

import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.GetAreaTextObjectsRequest;
import org.fractal.map.message.response.GetAreaTextObjectsResponse;
import org.fractal.map.message.response.TextObjectInfo;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.web.ServletError;
import org.fractal.map.web.response.AbstractInfo;
import org.fractal.map.web.response.LoadTextObjectsInfo;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

public class LoadTextObjectsMethod extends AbstractResponseWaitingMethod {

    @Override
    protected ServletMessage createMessage( Map<String, Object> requestParams, HttpSession httpSession ) {
        Double centerRe = ( Double ) httpSession.getAttribute( Names.CENTER_RE );
        Double centerIm = ( Double ) httpSession.getAttribute( Names.CENTER_IM );
        Integer currentLayerIndex = ( Integer ) httpSession.getAttribute( Names.CURRENT_LAYER_INDEX );
        int areaWidth = ( Integer ) requestParams.get( Names.AREA_WIDTH );
        int areaHeight = ( Integer ) requestParams.get( Names.AREA_HEIGHT );
        return new GetAreaTextObjectsRequest(
                UUID.randomUUID(), currentLayerIndex, centerRe, centerIm, areaWidth, areaHeight );
    }

    @Override
    protected AbstractInfo writeErrorResponse( ServletError error ) {
        LoadTextObjectsInfo loadTextObjectsInfo = new LoadTextObjectsInfo();
        loadTextObjectsInfo.setWasError( true );
        loadTextObjectsInfo.setError( error );
        return loadTextObjectsInfo;
    }

    @Override
    protected AbstractInfo writeReplyMessageToResponse( Transportable replyMessage ) {
        GetAreaTextObjectsResponse message = ( GetAreaTextObjectsResponse ) replyMessage;
        LoadTextObjectsInfo loadTextObjectsInfo = new LoadTextObjectsInfo();
        for ( TextObjectInfo textObjectInfo : message.getTextObjects() ) {
            loadTextObjectsInfo.addTextObject( textObjectInfo.getRe(), textObjectInfo.getIm(),
                    textObjectInfo.getCanvasLeftX(), textObjectInfo.getCanvasTopY(), textObjectInfo.getText() );
        }
        return loadTextObjectsInfo;
    }
}
