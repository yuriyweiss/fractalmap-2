package org.fractal.map.tests.calc;

import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.calc.request.SquareCalculator;
import org.fractal.map.conf.Configuration;
import org.fractal.map.message.request.SquareRequest;
import org.fractal.map.storage.combined.CombinedLoadSquareStrategy;
import org.fractal.map.storage.combined.CombinedSaveSquareStrategy;

import java.util.UUID;

public class TestRequestSquare {

    public static void main( String[] args ) throws Exception {
        System.setProperty( "log4j.configurationFile", "conf/log4j2-calcdisp.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/calcdisp.conf" );

        // Test calculation or DB load of upper square.
        int layerIndex = 3;
        double leftRe = -0.59375;
        double topIm = 0.46875;
        SquareRequest request = new SquareRequest( UUID.randomUUID(), layerIndex, leftRe, topIm );
        new SquareCalculator( request, new CombinedLoadSquareStrategy(), new CombinedSaveSquareStrategy() ).calculate();

        // Test coordinates symmetric conversion (from negative IM to positive IM).
        layerIndex = 3;
        // x = 17 (0 - first index)
        // y from top = 9 (0 - first index) => y = 32-9 = 23
        leftRe = 0.09375 * 17 - 2;
        topIm = 1.5 - 0.09375 * 22;
        request = new SquareRequest( UUID.randomUUID(), layerIndex, leftRe, topIm );
        new SquareCalculator( request, new CombinedLoadSquareStrategy(), new CombinedSaveSquareStrategy() ).calculate();
    }
}
