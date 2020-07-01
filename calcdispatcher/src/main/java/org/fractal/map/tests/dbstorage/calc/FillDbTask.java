package org.fractal.map.tests.dbstorage.calc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.square.SquaresPartition;
import org.fractal.map.conf.Configuration;
import org.fractal.map.context.Context;
import org.fractal.map.model.Layer;
import org.fractal.map.storage.oracle.SaveSquareToOracleStrategy;

import java.sql.Connection;

public class FillDbTask implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private final Connection conn;

    public FillDbTask( Connection conn ) {
        this.conn = conn;
    }

    @Override
    public void run() {
        try {
            logger.info( "DB calculation task started" );
            Layer layer = prepareLayer();
            createSquaresPartition( layer );
            calculateSquares();
            logger.info( "DB calculation task finished" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private Layer prepareLayer() {
        int layerIndex = Configuration.getLayerIndex();
        int iterations = Configuration.getIterationsCount();
        long layerSideSize = Configuration.getLayerSideSize();
        return new Layer( layerIndex, iterations, layerSideSize );
    }

    private void createSquaresPartition( Layer layer ) {
        SquaresPartition squaresPartition = new SquaresPartition( layer );
        Context.setSquaresPartition( squaresPartition );
    }

    private void calculateSquares() throws Exception {
        Context.getSquaresPartition().calculateAndSaveSquares( new SaveSquareToOracleStrategy( conn ) );
    }
}
