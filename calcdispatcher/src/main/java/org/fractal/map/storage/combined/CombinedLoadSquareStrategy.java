package org.fractal.map.storage.combined;

import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.conf.Configuration;
import org.fractal.map.model.Square;
import org.fractal.map.storage.disk.LoadSquareFromDiskStrategy;
import org.fractal.map.storage.mysql.LoadSquareFromMySQLStrategy;

public class CombinedLoadSquareStrategy implements LoadSquareStrategy {

    private final String rootDir;

    public CombinedLoadSquareStrategy() {
        this.rootDir = Configuration.getStorageDiskRootDir();
    }

    public CombinedLoadSquareStrategy( String rootDir ) {
        this.rootDir = rootDir;
    }

    @Override
    public Square loadSquare( int layerIndex, double leftRe, double topIm ) throws Exception {
        return new LoadSquareFromMySQLStrategy().loadSquare( layerIndex, leftRe, topIm );
    }

    @Override
    public byte[] loadSquareBody( Square square ) throws Exception {
        return new LoadSquareFromDiskStrategy( rootDir ).loadSquareBody( square );
    }
}
