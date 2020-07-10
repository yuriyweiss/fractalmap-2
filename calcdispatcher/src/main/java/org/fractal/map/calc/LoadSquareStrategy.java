package org.fractal.map.calc;

import org.fractal.map.model.Square;

import java.io.IOException;

public interface LoadSquareStrategy {

    Square loadSquare( final int layerIndex, final double leftRe, final double topIm );

    byte[] loadSquareBody( Square square ) throws IOException;
}
