package org.fractal.map.web.response;

import java.util.ArrayList;
import java.util.List;

public class SquarePartitionInfo extends AbstractInfo {

    private List<SquareInfo> squares = new ArrayList<>();

    public List<SquareInfo> getSquares() {
        return squares;
    }

    public void addSquare( double leftRe, double topIm, int canvasLeftX, int canvasTopY ) {
        squares.add( new SquareInfo( leftRe, topIm, canvasLeftX, canvasTopY ) );
    }
}
