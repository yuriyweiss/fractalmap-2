package org.fractal.map.tests.calc;

import org.fractal.map.calc.request.PointCoordsCalculator;
import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.request.PointCoordsRequest;
import org.fractal.map.message.response.PointCoordsResponse;

import java.util.UUID;

public class TestChangePointCoords {

    public static void main( String[] args ) {
        MessagesRegistrator.registerMessages();
        double re = 0;
        double im = 0;
        int shiftX = -100;
        int shiftY = -10;
        PointCoordsRequest request =
                new PointCoordsRequest( UUID.randomUUID(), 1, re, im, shiftX, shiftY );
        PointCoordsResponse response = new PointCoordsCalculator( request ).calculate();
        double newRe = -0.29296875;
        double newIm = -0.029296875;
        System.out.println( String.format( "estimated [re: %11.10f; im: %11.10f]", newRe, newIm ) );
        System.out.println( String.format( "calculated [re: %11.10f; im: %11.10f]", response.getRe(),
                response.getIm() ) );
    }
}
