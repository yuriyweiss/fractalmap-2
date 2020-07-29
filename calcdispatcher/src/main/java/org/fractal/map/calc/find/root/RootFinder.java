package org.fractal.map.calc.find.root;

import org.apache.commons.math3.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.conf.Configuration;
import org.fractal.map.message.request.FindRootRequest;
import org.fractal.map.message.response.FindRootResponse;
import org.fractal.map.storage.mysql.TextObjectToMysqlSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Try find root of Q(n) polynomial.<br>
 * Good search area must be selected. Otherwise no root will be returned at any error.<br>
 * Polynomial degree is limited by 100. If greater, then nearest to 0 iteration may be not present in lastIterations
 * array.
 */
@Component
public class RootFinder {

    private static final Logger logger = LogManager.getLogger();

    public static final double ZERO_APPROX = 1E-8;
    public static final int ITER_TO_STORE = 100;
    public static final int SEARCH_ROUNDS_LIMIT = 15;

    private TextObjectToMysqlSaver textObjectSaver;

    @Autowired
    public RootFinder( TextObjectToMysqlSaver textObjectSaver ) {
        this.textObjectSaver = textObjectSaver;
    }

    /**
     * Perform search and save result to database.<br>
     * There must be exactly one root in selected area for this procedure to return correct result.<br>
     * Return minimum iteration when zero reached or -1 if root not found.
     *
     * @param request root find request
     * @return prepared find root response holding -1 in case of some trouble
     */
    public FindRootResponse findRoot( FindRootRequest request ) {
        logger.info( "root search started for request: {}", request );
        Pair<Double, Double> rootCandidate =
                doFindRoot( 1, request.getLeftRe(), request.getBottomIm(), request.getRightRe(), request.getTopIm() );
        if ( rootCandidate == null ) {
            return new FindRootResponse( request.getRequestUUID(), -1 );
        }
        double rootRe = rootCandidate.getFirst();
        double rootIm = rootCandidate.getSecond();
        int degree = findMinPolynomialDegree( rootRe, rootIm );
        logger.info( "min polynomial degree: {}", degree );
        if ( degree != -1 ) {
            // root finding needs large scale, but text must be displayed on earlier layers
            int minLayerIndex = request.getLayerIndex() > 1 ? request.getLayerIndex() - 1 : 1;
            // save root info to DB
            textObjectSaver.save( minLayerIndex, rootRe, rootIm, Integer.toString( degree ) );
        }
        return new FindRootResponse( request.getRequestUUID(), degree );
    }

    /**
     * Recursive root finding procedure, narrowing search area.
     *
     * @param currentRound current depth of recursion
     * @param leftRe       left Re coordinate of search area
     * @param topIm        top Im coordinate of search area
     * @param rightRe      right Re coordinate of search area
     * @param bottomIm     bottom Im coordinate of search area
     * @return coordinates of root candidate; if error, then null
     */
    public Pair<Double, Double> doFindRoot( int currentRound, double leftRe, double bottomIm, double rightRe,
            double topIm ) {
        double width = rightRe - leftRe;
        double height = topIm - bottomIm;
        double stepX = width / 9;
        double stepY = height / 9;
        double[][] minimums = fillMinimums( leftRe, bottomIm, stepX, stepY );
        Pair<Integer, Integer> minimumPos = findMinimumPos( minimums );
        int posY = minimumPos.getFirst();
        int posX = minimumPos.getSecond();
        // check if recursion must be stopped
        if ( minimums[posY][posX] == Double.MAX_VALUE ) {
            // user selected area out of Mandelbrot set
            logger.info( "currentRound: {}, all area points are out of Mandelbrot set", currentRound );
            return null;
        }
        if ( minimums[posY][posX] < ZERO_APPROX ) {
            // root found
            logger.info( "currentRound: {}, iteration minimum fits ZERO_APPROX: {}", currentRound,
                    minimums[posY][posX] );
            return Pair.create( leftRe + posX * stepX, bottomIm + posY * stepY );
        } else if ( currentRound == SEARCH_ROUNDS_LIMIT ) {
            // desirable approximation to 0 not reached in 15 rounds
            logger.info( "currentRound: {}, ZERO_APPROX not reached, minimum: {}", currentRound,
                    minimums[posY][posX] );
            return null;
        }
        logger.info( "currentRound: {}, minimum: [{}] found at: [x: {}, y: {}]", currentRound, minimums[posY][posX],
                posX, posY );
        // call recursion with narrowed search area
        return doFindRoot( currentRound + 1,
                leftRe + limitToBounds( posX - 1 ) * stepX,
                bottomIm + limitToBounds( posY - 1 ) * stepY,
                leftRe + limitToBounds( posX + 1 ) * stepX,
                bottomIm + limitToBounds( posY + 1 ) * stepY );
    }

    /**
     * Perform search of nearest to 0 iteration for each point in selected area.<br>
     * Area array size is constant and is 10x10.
     *
     * @param leftRe   starting Re coordinate
     * @param bottomIm starting Im coordinate
     * @param stepX    step by Re axis
     * @param stepY    step by Im axis
     * @return filled array of nearest to 0 iterations for each point in area
     */
    private double[][] fillMinimums( double leftRe, double bottomIm, double stepX, double stepY ) {
        double[][] minimums = new double[10][10];
        for ( int i = 0; i < 10; i++ ) {
            for ( int j = 0; j < 10; j++ ) {
                minimums[i][j] = findNearestToZeroIteration( leftRe + j * stepX, bottomIm + i * stepY );
            }
        }
        return minimums;
    }

    /**
     * Assuming that selected area is in hyperbolic component of Mandelbrot set, each point iterations should have
     * cyclic manner.<br>
     * So after 1500 iterations we expect good convergence to cyclic sequence of points.
     *
     * @param re real point coordinate
     * @param im imaginary point coordinate
     * @return minimal distance of iterating point from 0 or MAX_DOUBLE, if point is out of Mandelbrot set
     */
    private double findNearestToZeroIteration( double re, double im ) {
        double prevRe = re;
        double prevIm = im;
        int maxIterations = Configuration.getIterationsCount();
        double[] lastIterations = new double[ITER_TO_STORE];
        for ( int i = 0; i < ITER_TO_STORE; i++ ) {
            lastIterations[i] = Double.MAX_VALUE;
        }
        for ( int i = 0; i < maxIterations; i++ ) {
            double currRe = prevRe * prevRe - prevIm * prevIm + re;
            double currIm = 2 * prevRe * prevIm + im;
            double norm = currRe * currRe + currIm * currIm;
            prevRe = currRe;
            prevIm = currIm;
            if ( i >= maxIterations - ITER_TO_STORE ) {
                int index = i - ( maxIterations - ITER_TO_STORE );
                lastIterations[index] = Math.sqrt( norm );
            }
            if ( norm > 10 ) {
                return Double.MAX_VALUE;
            }
        }
        return Arrays.stream( lastIterations ).min().getAsDouble();
    }

    /**
     * Find position of minimum in minimums array.
     *
     * @param minimums array of minimums
     * @return minimum position
     */
    private Pair<Integer, Integer> findMinimumPos( double[][] minimums ) {
        int resI = 0;
        int resJ = 0;
        double min = minimums[0][0];
        for ( int i = 0; i < 10; i++ ) {
            for ( int j = 0; j < 10; j++ ) {
                if ( minimums[i][j] < min ) {
                    resI = i;
                    resJ = j;
                    min = minimums[i][j];
                }
            }
        }
        return Pair.create( resI, resJ );
    }

    /**
     * Preserve calculated array index from breaking the bounds of array.
     *
     * @param index calculated index
     * @return bounded index
     */
    private int limitToBounds( int index ) {
        if ( index < 0 ) {
            return 0;
        } else if ( index > 9 ) {
            return 9;
        } else {
            return index;
        }
    }

    /**
     * Perform Mandelbrot iterations until zero point reached.<br>
     * Iteration when zero point reached is result of our search. If predefined approximation of zero is unreachable
     * in 100 iterations, then return -1.
     *
     * @param re real coordinate of root candidate
     * @param im imaginary coordinate of root candidate
     * @return zero reached iteration or -1 if zerg not reached
     */
    private int findMinPolynomialDegree( Double re, Double im ) {
        logger.trace( "started findMinPolynomialDegree() for [re: {}, im: {}]", re, im );
        double prevRe = re;
        double prevIm = im;
        int currentDegree = 0;
        boolean zeroFound = false;
        while ( !zeroFound ) {
            currentDegree++;
            double currRe = prevRe * prevRe - prevIm * prevIm + re;
            double currIm = 2 * prevRe * prevIm + im;
            double modulo = Math.sqrt( currRe * currRe + currIm * currIm );
            logger.trace( "degree: {}, re: {}, im: {}, modulo: {}", currentDegree, currRe, currIm, modulo );
            prevRe = currRe;
            prevIm = currIm;
            if ( modulo <= ZERO_APPROX ) {
                zeroFound = true;
            }
            if ( currentDegree > 100 ) {
                return -1;
            }
        }
        return currentDegree;
    }
}
