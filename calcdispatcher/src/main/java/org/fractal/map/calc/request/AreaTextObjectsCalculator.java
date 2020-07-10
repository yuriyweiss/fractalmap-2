package org.fractal.map.calc.request;

import org.apache.commons.math3.util.Pair;
import org.fractal.map.calc.CalcUtils;
import org.fractal.map.message.request.GetAreaTextObjectsRequest;
import org.fractal.map.message.response.GetAreaTextObjectsResponse;
import org.fractal.map.message.response.TextObjectInfo;
import org.fractal.map.model.Layer;
import org.fractal.map.model.LayerRegistry;
import org.fractal.map.storage.mysql.TextObjectsFromMysqlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AreaTextObjectsCalculator {

    private TextObjectsFromMysqlLoader textObjectsFromMysqlLoader;

    @Autowired
    public AreaTextObjectsCalculator( TextObjectsFromMysqlLoader textObjectsFromMysqlLoader ) {
        this.textObjectsFromMysqlLoader = textObjectsFromMysqlLoader;
    }

    public GetAreaTextObjectsResponse calculate( GetAreaTextObjectsRequest request ) {
        Layer layer = LayerRegistry.getLayerByIndex( request.getLayerIndex() );
        double pointWidth = layer.getPointWidth();

        int areaHalfX = ( int ) Math.ceil( request.getAreaSizeX() / 2.0 );
        int areaHalfY = ( int ) Math.ceil( request.getAreaSizeY() / 2.0 );

        double leftRe = CalcUtils.limitRe( request.getRe() - pointWidth * areaHalfX );
        double rightRe = CalcUtils.limitRe( request.getRe() + pointWidth * areaHalfX );
        double topIm = CalcUtils.limitIm( request.getIm() + pointWidth * areaHalfY );
        double bottomIm = CalcUtils.limitIm( request.getIm() - pointWidth * areaHalfY );

        List<TextObjectInfo> textObjects = textObjectsFromMysqlLoader.load( layer.getLayerIndex(), leftRe, rightRe,
                bottomIm, topIm );

        GetAreaTextObjectsResponse result =
                new GetAreaTextObjectsResponse( request.getRequestUUID() );

        Pair<Integer, Integer> canvasShift = calcCanvasShift( layer, leftRe, topIm, request.getRe(), request.getIm() );
        for ( TextObjectInfo textObjectInfo : textObjects ) {
            int canvasX = ( int ) Math.round( ( textObjectInfo.getRe() - leftRe ) / layer.getPointWidth() ) +
                    canvasShift.getFirst();
            int canvasY = ( int ) Math.round( ( topIm - textObjectInfo.getIm() ) / layer.getPointWidth() ) +
                    canvasShift.getSecond();
            textObjectInfo.setCanvasX( canvasX );
            textObjectInfo.setCanvasY( canvasY );
            result.addTextObjectInfo( textObjectInfo );
        }

        return result;
    }

    private Pair<Integer, Integer> calcCanvasShift( Layer layer, double leftRe, double topIm, double requestRe,
            double requestIm ) {
        int xDelta = ( int ) Math.round( ( requestRe - leftRe ) / layer.getPointWidth() );
        int yDelta = ( int ) Math.round( ( topIm - requestIm ) / layer.getPointWidth() );
        return Pair.create( xDelta, yDelta );
    }
}
