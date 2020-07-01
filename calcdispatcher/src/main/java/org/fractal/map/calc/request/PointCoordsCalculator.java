package org.fractal.map.calc.request;

import org.fractal.map.calc.CalcUtils;
import org.fractal.map.message.request.PointCoordsRequest;
import org.fractal.map.message.response.PointCoordsResponse;
import org.fractal.map.model.Layer;
import org.fractal.map.model.LayerRegistry;

public class PointCoordsCalculator {
    private final PointCoordsRequest request;
    private Layer layer;

    public PointCoordsCalculator( PointCoordsRequest request ) {
        this.request = request;
    }

    public PointCoordsResponse calculate() {
        layer = LayerRegistry.getLayerByIndex( request.getLayerIndex() );
        double shiftedRe =
                CalcUtils.limitRe( request.getRe() + request.getShiftX() * layer.getPointWidth() );
        double shiftedIm =
                CalcUtils.limitIm( request.getIm() + request.getShiftY() * layer.getPointWidth() );
        return new PointCoordsResponse( request.getRequestUUID(), shiftedRe, shiftedIm );
    }
}
