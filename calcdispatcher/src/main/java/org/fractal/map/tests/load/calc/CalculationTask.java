package org.fractal.map.tests.load.calc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.square.SquaresPartition;
import org.fractal.map.conf.Configuration;
import org.fractal.map.context.Context;
import org.fractal.map.model.Layer;
import org.fractal.map.storage.combined.CombinedSaveSquareStrategy;

public class CalculationTask implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private final String rootDir;

    private Layer layer;

    public CalculationTask( String rootDir ) {
        this.rootDir = rootDir;
    }

    @Override
    public void run() {
        logger.info( "FILE calculation task started" );
        layer = prepareLayer();
        Context.getBaseCounters().setSquaresToCalculate(
                layer.getSquaresBySide() * layer.getSquaresBySide() / 2 );
        createSquaresPartition();
        calculateSquares();
        logger.info( "FILE calculation task finished" );
    }

    private Layer prepareLayer() {
        int layerIndex = Configuration.getLayerIndex();
        int iterations = Configuration.getIterationsCount();
        long layerSideSize = Configuration.getLayerSideSize();
        return new Layer( layerIndex, iterations, layerSideSize );
    }

    private void createSquaresPartition() {
        SquaresPartition squaresPartition = new SquaresPartition( layer );
        Context.setSquaresPartition( squaresPartition );
    }

    private void calculateSquares() {
        Context.getSquaresPartition().calculateAndSaveSquares(
                new CombinedSaveSquareStrategy( rootDir ) );
    }
}
