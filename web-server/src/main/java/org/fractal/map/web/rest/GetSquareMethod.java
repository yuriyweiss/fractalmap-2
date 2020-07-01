package org.fractal.map.web.rest;

import com.google.gson.GsonBuilder;
import org.fractal.map.calc.Constants;
import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.SquareRequest;
import org.fractal.map.message.response.CalcErrorResponse;
import org.fractal.map.message.response.SquareResponse;
import org.fractal.map.transceiver.Transportable;
import org.fractal.map.web.ServletError;
import org.fractal.map.web.response.AbstractInfo;
import org.fractal.map.web.response.SquareBodyInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

@Component
public class GetSquareMethod extends AbstractResponseWaitingMethod {

    @Override
    protected ServletMessage createMessage( Map<String, Object> requestParams, HttpSession httpSession ) {
        double leftRe = ( Double ) requestParams.get( Names.LEFT_RE );
        double topIm = ( Double ) requestParams.get( Names.TOP_IM );
        Integer currentLayerIndex = ( Integer ) httpSession.getAttribute( Names.CURRENT_LAYER_INDEX );
        return new SquareRequest( UUID.randomUUID(), currentLayerIndex, leftRe, topIm );
    }

    @Override
    protected AbstractInfo writeErrorResponse( ServletError error ) {
        SquareBodyInfo squareBodyInfo = new SquareBodyInfo();
        squareBodyInfo.setWasError( true );
        squareBodyInfo.setError( error );
        return squareBodyInfo;
    }

    @Override
    protected AbstractInfo writeReplyMessageToResponse( Transportable replyMessage ) {
        if ( replyMessage instanceof CalcErrorResponse ) {
            return writeErrorResponse( ServletError.SQUARE_BODY_NOT_AVAILABLE );
        }
        SquareResponse message = ( SquareResponse ) replyMessage;
        if ( message.getIterations() != Constants.ITERATIONS_DIFFER ) {
            return new SquareBodyInfo( message.getIterations() );
        } else if ( message.getSquareBody() != null ) {
            return writeUnzippedBodyResponse( message.getSquareBody() );
        } else {
            return writeErrorResponse( ServletError.SQUARE_BODY_NOT_AVAILABLE );
        }
    }

    private SquareBodyInfo writeUnzippedBodyResponse( byte[] squareBody ) {
        SquareBodyInfo squareBodyInfo = new SquareBodyInfo( Constants.ITERATIONS_DIFFER );
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader( new GZIPInputStream( new ByteArrayInputStream( squareBody ) ),
                            StandardCharsets.UTF_8 ) );
            int[][] points = new GsonBuilder().create().fromJson( reader, int[][].class );
            squareBodyInfo.setPoints( points );
        } catch ( IOException e ) {
            squareBodyInfo.setWasError( true );
            squareBodyInfo.setError( ServletError.SQUARE_BODY_NOT_AVAILABLE );
        }
        return squareBodyInfo;
    }
}
