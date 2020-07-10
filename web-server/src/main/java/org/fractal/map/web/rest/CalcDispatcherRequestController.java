package org.fractal.map.web.rest;

import org.fractal.map.calc.Constants;
import org.fractal.map.model.Layer;
import org.fractal.map.model.LayerRegistry;
import org.fractal.map.web.response.AbstractInfo;
import org.fractal.map.web.response.InitializeFormInfo;
import org.fractal.map.web.response.LayerInfo;
import org.fractal.map.web.transceiver.WebServerTransceiverClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CalcDispatcherRequestController {

    private WebServerTransceiverClient webServerTransceiverClient;
    private SquaresPartitionMethod squaresPartitionMethod;
    private GetSquareMethod getSquareMethod;
    private GetPointCoordsMethod getPointCoordsMethod;
    private LoadTextObjectsMethod loadTextObjectsMethod;

    @Autowired
    public CalcDispatcherRequestController(
            WebServerTransceiverClient webServerTransceiverClient,
            SquaresPartitionMethod squaresPartitionMethod,
            GetSquareMethod getSquareMethod,
            GetPointCoordsMethod getPointCoordsMethod,
            LoadTextObjectsMethod loadTextObjectsMethod ) {
        this.webServerTransceiverClient = webServerTransceiverClient;
        this.squaresPartitionMethod = squaresPartitionMethod;
        this.getSquareMethod = getSquareMethod;
        this.getPointCoordsMethod = getPointCoordsMethod;
        this.loadTextObjectsMethod = loadTextObjectsMethod;
    }

    @GetMapping( path = "/initialize-form" )
    public InitializeFormInfo initializeForm( HttpSession httpSession ) {
        if ( httpSession.isNew() || httpSession.getAttribute( Names.CENTER_RE ) == null ) {
            Double centerRe = ( Constants.RE_RIGHT + Constants.RE_LEFT ) / 2;
            Double centerIm = ( Constants.IM_TOP + Constants.IM_BOTTOM ) / 2;
            httpSession.setAttribute( Names.CENTER_RE, centerRe );
            httpSession.setAttribute( Names.CENTER_IM, centerIm );
            httpSession.setAttribute( Names.CURRENT_LAYER_INDEX, 1 );
        }
        Double centerRe = ( Double ) httpSession.getAttribute( Names.CENTER_RE );
        Double centerIm = ( Double ) httpSession.getAttribute( Names.CENTER_IM );
        Integer currentLayerIndex = ( Integer ) httpSession.getAttribute( Names.CURRENT_LAYER_INDEX );
        InitializeFormInfo initializeFormInfo =
                new InitializeFormInfo( centerRe, centerIm, currentLayerIndex );
        for ( Layer layer : LayerRegistry.getLayers() ) {
            initializeFormInfo.addLayer( new LayerInfo( layer.getLayerIndex() ) );
        }
        return initializeFormInfo;
    }

    @GetMapping( path = "/change-viewport-params" )
    public void changeViewportParams(
            @RequestParam( name = "re" ) double re,
            @RequestParam( name = "im" ) double im,
            @RequestParam( name = "layerIndex" ) int layerIndex,
            HttpSession httpSession ) {
        // return if session does not exist, form must be initialized
        if ( httpSession.isNew() ) {
            return;
        }
        httpSession.setAttribute( Names.CENTER_RE, re );
        httpSession.setAttribute( Names.CENTER_IM, im );
        httpSession.setAttribute( Names.CURRENT_LAYER_INDEX, layerIndex );
    }

    @GetMapping( path = "/squares-partition" )
    public AbstractInfo getSquaresPartition(
            @RequestParam( name = "areaWidth" ) int areaWidth,
            @RequestParam( name = "areaHeight" ) int areaHeight,
            HttpSession httpSession ) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put( Names.AREA_WIDTH, areaWidth );
        requestParams.put( Names.AREA_HEIGHT, areaHeight );
        return squaresPartitionMethod.processGetRequest( requestParams, httpSession, webServerTransceiverClient );
    }

    @GetMapping( path = "/get-square" )
    public AbstractInfo getSquare(
            @RequestParam( name = "leftRe" ) double leftRe,
            @RequestParam( name = "topIm" ) double topIm,
            HttpSession httpSession ) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put( Names.LEFT_RE, leftRe );
        requestParams.put( Names.TOP_IM, topIm );
        return getSquareMethod.processGetRequest( requestParams, httpSession, webServerTransceiverClient );
    }

    @GetMapping( path = "/get-point-coords" )
    public AbstractInfo getPointCoords(
            @RequestParam( name = "shiftX" ) int shiftX,
            @RequestParam( name = "shiftY" ) int shiftY,
            HttpSession httpSession ) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put( Names.SHIFT_X, shiftX );
        requestParams.put( Names.SHIFT_Y, shiftY );
        return getPointCoordsMethod.processGetRequest( requestParams, httpSession, webServerTransceiverClient );
    }

    @GetMapping( path = "/load-text-objects" )
    public AbstractInfo loadTextObjects(
            @RequestParam( name = "areaWidth" ) int areaWidth,
            @RequestParam( name = "areaHeight" ) int areaHeight,
            HttpSession httpSession ) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put( Names.AREA_WIDTH, areaWidth );
        requestParams.put( Names.AREA_HEIGHT, areaHeight );
        return loadTextObjectsMethod.processGetRequest( requestParams, httpSession, webServerTransceiverClient );
    }
}
