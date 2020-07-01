package org.fractal.map.tests.norms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;

public class NormsCalculator {
    private static List<Complex> points = new ArrayList<>();
    private static DecimalFormat df;
    private static ComplexFormat cf;

    static {
        points.add( new Complex( -1.7546875, 0 ) ); // 1
        points.add( new Complex( -1.1394531, 0.24414063 ) ); // 2
        points.add( new Complex( -0.1300781, 0.7613281 ) ); // 2
        points.add( new Complex( 0.27929688, 0.53554688 ) ); // 3
        points.add( new Complex( 0.276367186, 0.472827148 ) ); // many

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator( '.' );
        df = new DecimalFormat( "0.00000", dfs );
        df.setMinimumIntegerDigits( 1 );
        df.setMinimumFractionDigits( 5 );
        df.setMaximumIntegerDigits( 5 );
        df.setMaximumFractionDigits( 9 );
        cf = new ComplexFormat( df );
    }

    public static void calculateNorms( Complex c, BufferedWriter writer ) throws IOException {
        System.out.println( "c: " + cf.format( c ) );
        writer.write( cf.format( c ) );
        writer.newLine();
        writer.newLine();

        Complex zPrev = new Complex( c.getReal(), c.getImaginary() );
        Complex zCurr;
        int iterCount = 0;
        while ( iterCount < 500 ) {
            zCurr = zPrev.multiply( zPrev );
            zCurr = zCurr.add( c );
            System.out.println( iterCount + ": " + cf.format( zCurr ) );
            writer.write( cf.format( zCurr ) );
            writer.newLine();

            double abs = zCurr.abs();
            zPrev = zCurr;
            System.out.println( "abs: " + df.format( abs ) );
            if ( abs > 4 ) {
                break;
            }
            iterCount++;
        }
    }

    public static void main( String[] args ) throws IOException {
        for ( int i = 0; i < points.size(); i++ ) {
            Complex point = points.get( i );
            BufferedWriter writer =
                    new BufferedWriter( new FileWriter( "norms/" + i + ".txt", false ) );
            calculateNorms( point, writer );
            writer.flush();
            writer.close();
        }
    }
}
