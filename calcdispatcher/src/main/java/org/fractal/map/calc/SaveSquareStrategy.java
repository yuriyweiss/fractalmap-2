package org.fractal.map.calc;

import org.fractal.map.model.Square;

import java.io.IOException;

public interface SaveSquareStrategy {

    void save( Square square, int[][] points ) throws IOException;

    void save( Square square, byte[] squareBody ) throws IOException;
}
