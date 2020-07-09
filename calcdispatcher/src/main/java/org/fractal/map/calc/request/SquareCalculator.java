package org.fractal.map.calc.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.CalcUtils;
import org.fractal.map.calc.Constants;
import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.message.ServletMessage;
import org.fractal.map.message.request.SquareRequest;
import org.fractal.map.message.response.CalcErrorResponse;
import org.fractal.map.message.response.ErrorCodes;
import org.fractal.map.message.response.SquareResponse;
import org.fractal.map.model.Layer;
import org.fractal.map.model.LayerRegistry;
import org.fractal.map.model.Square;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.fractal.map.calc.Constants.SQUARE_SIDE_SIZE;

public class SquareCalculator {

    private static final Logger logger = LogManager.getLogger();

    private static final String ERROR_STACKTRACE = "error stacktrace";

    private final SquareRequest request;
    private final LoadSquareStrategy loadStrategy;
    private final SaveSquareStrategy saveStrategy;
    private final Layer layer;

    private Square square;

    public SquareCalculator( SquareRequest request, LoadSquareStrategy loadStrategy,
            SaveSquareStrategy saveStrategy ) {
        this.request = request;
        this.layer = LayerRegistry.getLayerByIndex( request.getLayerIndex() );
        this.loadStrategy = loadStrategy;
        this.saveStrategy = saveStrategy;
    }

    public ServletMessage calculate() {
        double topIm = request.getTopIm();
        if ( topIm <= 0 ) {
            topIm = moveToPositiveHalfPlane( topIm );
        }
        loadSquare( topIm );
        if ( square != null ) {
            logger.debug( "square loaded from DB" );
            ServletMessage result =
                    new SquareResponse( request.getRequestUUID(), square.getIterations() );
            if ( square.getIterations() == Constants.ITERATIONS_DIFFER ) {
                logger.debug( "need load square body" );
                try {
                    byte[] body = loadStrategy.loadSquareBody( square );
                    logger.debug( "square body file found" );
                    ( ( SquareResponse ) result ).setSquareBody( body );
                } catch ( FileNotFoundException e ) {
                    logger.debug( "square body file not found, perform calculation" );
                    result = calculateAndSaveSquare();
                } catch ( Exception e ) {
                    logger.debug( "unknown error while body loading", e );
                    result =
                            new CalcErrorResponse( request.getRequestUUID(), ErrorCodes.UNKNOWN_ERROR );
                }
            }
            return result;
        } else {
            square = new Square( layer, request.getLeftRe(), topIm );
            return calculateAndSaveSquare();
        }
    }

    private void loadSquare( double topIm ) {
        try {
            square = loadStrategy.loadSquare( request.getLayerIndex(), request.getLeftRe(), topIm );
        } catch ( Exception e ) {
            logger.error( "square not loaded: [layerIndex: {}, leftRe: {}, topIm: {}]",
                    request.getLayerIndex(), request.getLeftRe(), topIm );
            logger.debug( ERROR_STACKTRACE, e );
            square = null;
        }
    }

    private double moveToPositiveHalfPlane( double topIm ) {
        double result = -1 * topIm;
        result += layer.getSquareWidth();
        return result;
    }

    private ServletMessage calculateAndSaveSquare() {
        int[][] points = calculateSquare();
        int iterations = CalcUtils.getCommonIteration( points );
        square.setIterations( iterations );
        SquareResponse result =
                new SquareResponse( request.getRequestUUID(), square.getIterations() );
        if ( iterations == Constants.ITERATIONS_DIFFER ) {
            logger.debug( "need save body to file system" );
            try {
                byte[] body = compressPoints( points );
                saveStrategy.save( square, body );
                result.setSquareBody( body );
            } catch ( Exception e ) {
                logger.error( "square not saved: {}", square );
                logger.debug( ERROR_STACKTRACE, e );
                return new CalcErrorResponse( request.getRequestUUID(), ErrorCodes.SQUARE_BODY_SAVE_ERROR );
            }
        } else {
            logger.debug( "save square only to DB" );
            try {
                saveStrategy.save( square, ( byte[] ) null );
            } catch ( Exception e ) {
                logger.error( "square not saved: {}", square );
                logger.debug( ERROR_STACKTRACE, e );
                // return correct result, square not saved, but calculation not failed
            }
        }
        return result;
    }

    private int[][] calculateSquare() {
        int[][] result = new int[SQUARE_SIDE_SIZE][SQUARE_SIDE_SIZE];
        CalcUtils.initPointsNeedCalculation( result );
        square.calculatePoints( result );
        return result;
    }

    private byte[] compressPoints( int[][] points ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CalcUtils.saveAndCompressResultToStream( bos, points );
        return bos.toByteArray();
    }
}
