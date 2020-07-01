package org.fractal.map.calc;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

import static org.fractal.map.calc.Constants.ITERATIONS_DIFFER;
import static org.fractal.map.calc.Constants.SQUARE_SIDE_SIZE;

public class CalcUtils {

    public static void initPointsNeedCalculation( int[][] points ) {
        for ( int y = 0; y < SQUARE_SIDE_SIZE; y++ ) {
            for ( int x = 0; x < SQUARE_SIDE_SIZE; x++ ) {
                points[y][x] = Constants.POINT_NEEDS_CALC;
            }
        }
    }

    public static int getCommonIteration( int[][] points ) {
        int result = points[0][0];
        for ( int y = 0; y < Constants.SQUARE_SIDE_SIZE; y++ ) {
            for ( int x = 0; x < Constants.SQUARE_SIDE_SIZE; x++ ) {
                if ( points[y][x] != result ) return ITERATIONS_DIFFER;
            }
        }
        return result;
    }

    public static void saveAndCompressResultToStream( OutputStream os, int[][] points )
            throws IOException {
        OutputStreamWriter osw =
                new OutputStreamWriter( new GZIPOutputStream( os ), StandardCharsets.UTF_8 );
        osw.write( new Gson().toJson( points ) );
        osw.flush();
        osw.close();
    }

    public static void saveSquareBodyToStream( OutputStream os, byte[] squareBody )
            throws IOException {
        DataOutputStream dos = new DataOutputStream( os );
        dos.write( squareBody );
        dos.flush();
        dos.close();
    }

    public static double limitRe( double value ) {
        return limitValue( value, Constants.RE_LEFT, Constants.RE_RIGHT );
    }

    public static double limitIm( double value ) {
        return limitValue( value, Constants.IM_BOTTOM, Constants.IM_TOP );
    }

    private static double limitValue( double value, double lowerLimit, double upperLimit ) {
        double result = value;
        if ( value < lowerLimit ) {
            result = lowerLimit;
        }
        if ( value > upperLimit ) {
            result = upperLimit;
        }
        return result;
    }
}
