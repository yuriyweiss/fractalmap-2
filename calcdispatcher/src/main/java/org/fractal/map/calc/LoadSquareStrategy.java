package org.fractal.map.calc;

import org.fractal.map.model.Square;

public interface LoadSquareStrategy {

    Square loadSquare( int layerIndex, double leftRe, double topIm ) throws Exception;

    byte[] loadSquareBody( Square square ) throws Exception;
}
