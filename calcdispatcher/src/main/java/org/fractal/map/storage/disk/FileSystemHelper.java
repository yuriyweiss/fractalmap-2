package org.fractal.map.storage.disk;

import org.fractal.map.model.Layer;
import org.fractal.map.model.Square;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSystemHelper {

    private final Square square;
    private final String rootDir;
    private final List<String> dirStructure;

    public FileSystemHelper( Square square, String rootDir ) {
        this.square = square;
        this.rootDir = rootDir;
        this.dirStructure = buildDirStructure( square );
    }

    private List<String> buildDirStructure( Square square ) {
        List<String> result = new ArrayList<>();
        Layer layer = square.getLayer();
        result.add( "layer_" + layer.getLayerIndex() );
        long squareIndex = square.getSquareIndex();

        long squareCount = layer.getSquaresBySide() * layer.getSquaresBySide() / 2;
        long maxThousandPower = 1000;
        while ( maxThousandPower < squareCount ) {
            maxThousandPower *= 1000;
        }

        if ( maxThousandPower == 1000 ) {
            result.add( "000" );
        } else {
            long remainder = squareIndex;
            long divided;
            do {
                maxThousandPower /= 1000;
                divided = remainder / maxThousandPower;
                result.add( String.format( "%03d", divided ) );
                remainder = remainder % maxThousandPower;
            }
            while ( maxThousandPower > 1000 );
        }

        return result;
    }

    public String getFileName() {
        return getDirNameForSquare() + "/" + square.getSquareIndex() + "_x_" + square.getLayerX()
                + "_y_" + square.getLayerY() + ".dat";
    }

    private String getDirNameForSquare() {
        return buildDirName( dirStructure );
    }

    private String buildDirName( List<String> dirStructure ) {
        StringBuilder result = new StringBuilder( rootDir );
        for ( String dirName : dirStructure ) {
            result.append( "/" ).append( dirName );
        }
        return result.toString();
    }

    public void createDirectories() {
        StringBuilder currentDirName = new StringBuilder( rootDir );
        for ( String dirName : dirStructure ) {
            currentDirName.append( "/" ).append( dirName );
            if ( !isDirExists( currentDirName.toString() ) ) {
                File newDir = new File( currentDirName.toString() );
                newDir.mkdir();
            }
        }
    }

    private boolean isDirExists( String dirName ) {
        return new File( dirName ).exists();
    }
}
