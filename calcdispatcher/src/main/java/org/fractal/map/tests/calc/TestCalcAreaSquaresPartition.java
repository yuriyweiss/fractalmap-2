package org.fractal.map.tests.calc;

import org.fractal.map.calc.request.SquaresPartitionCalculator;
import org.fractal.map.message.MessagesRegistrator;
import org.fractal.map.message.request.AreaSquarePartitionRequest;
import org.fractal.map.message.response.AreaSquarePartitionResponse;
import org.fractal.map.message.response.SquareInfo;
import org.fractal.map.model.Layer;
import org.fractal.map.model.LayerRegistry;
import org.fractal.map.model.Square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TestCalcAreaSquaresPartition {

    public static void main( String[] args ) {
        MessagesRegistrator.registerMessages();
        double re = -0.5;
        double im = 0.3434;
        int areaSizeX = 957;
        int areaSizeY = 753;

        // 957 / 2 + 1 = 479;
        // 753 / 2 + 1 = 377;

        // leftRe = -0.5 - 0.00018310546875 * 479 = -0.58770751953125
        // rightRe = -0.5 + 0.00018310546875 * 479 = -0.41229248046875
        // leftCount = floor((-0.58770751953125 - (-2))/0.09375) = 15
        // leftSquareIndex = 16
        // rightCount = floor((1 - (-0.41229248046875))/0.09375) = 15
        // rightSquareIndex = 32 - 15 = 17

        // leftRe[] = { -2 + 0.09375 * (16-1) = -0.59375,
        //				-2 + 0.09375 * (17-1) = -0.5 }

        // topIm = 0.3434 + 0.00018310546875 * 377 = 0.41243076171875
        // bottomIm = 0.3434 - 0.00018310546875 * 377 = 0.27436923828125
        // topCount = floor((1.5 - 0.41243076171875)/0.09375) = 11
        // topSquareIndex = 12
        // bottomCount = floor((0,27436923828125 - (-1.5))/0.09375) = 18
        // bottomSquareIndex = 32 - 18 = 14

        // topIm[] = { 1.5 - 0.09375 * (12-1) = 0.46875,
        //				1.5 - 0.09375 * (13-1) = 0.375,
        //				1.5 - 0.09375 * (14-1) = 0.28125 }

        AreaSquarePartitionRequest request =
                new AreaSquarePartitionRequest( UUID.randomUUID(), 3, re, im, areaSizeX, areaSizeY );
        AreaSquarePartitionResponse response =
                new SquaresPartitionCalculator( request ).calculate();

        Layer layer = LayerRegistry.getLayerByIndex( 3 );
        List<Square> partition = new ArrayList<>();
        for ( SquareInfo squareInfo : response.getSquares() ) {
            partition.add( new Square( layer, squareInfo.getLeftRe(), squareInfo.getTopIm() ) );
        }
        Collections.sort( partition );

        List<Square> expectedList = new ArrayList<>();
        expectedList.add( new Square( layer, -0.59375, 0.46875 ) );
        expectedList.add( new Square( layer, -0.5, 0.46875 ) );
        expectedList.add( new Square( layer, -0.59375, 0.375 ) );
        expectedList.add( new Square( layer, -0.5, 0.375 ) );
        expectedList.add( new Square( layer, -0.59375, 0.28125 ) );
        expectedList.add( new Square( layer, -0.5, 0.28125 ) );

        for ( int i = 0; i < 6; i++ ) {
            Square calcResult = partition.get( i );
            Square expected = expectedList.get( i );
            if ( calcResult.equals( expected ) ) {
                System.out.println( "squares equal: " + expected );
            } else {
                System.out.println( "squares differ: \n" + expected + "\n" + calcResult );
            }
        }
    }
}
