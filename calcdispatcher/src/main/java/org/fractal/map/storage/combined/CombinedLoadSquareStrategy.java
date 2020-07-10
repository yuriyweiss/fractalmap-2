package org.fractal.map.storage.combined;

import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.conf.Configuration;
import org.fractal.map.launcher.ApplicationContextHolder;
import org.fractal.map.model.Square;
import org.fractal.map.storage.disk.LoadSquareFromDiskStrategy;
import org.fractal.map.storage.mysql.LoadSquareFromMysqlStrategy;

import java.io.IOException;

public class CombinedLoadSquareStrategy implements LoadSquareStrategy {

    private final LoadSquareFromMysqlStrategy loadSquareFromMySQLStrategy;
    private final String rootDir;

    public CombinedLoadSquareStrategy() {
        this( Configuration.getStorageDiskRootDir() );
    }

    public CombinedLoadSquareStrategy( String rootDir ) {
        this.rootDir = rootDir;
        this.loadSquareFromMySQLStrategy =
                ApplicationContextHolder.getApplicationContext().getBean( LoadSquareFromMysqlStrategy.class );
    }

    @Override
    public Square loadSquare( int layerIndex, double leftRe, double topIm ) {
        return loadSquareFromMySQLStrategy.loadSquare( layerIndex, leftRe, topIm );
    }

    @Override
    public byte[] loadSquareBody( Square square ) throws IOException {
        return new LoadSquareFromDiskStrategy( rootDir ).loadSquareBody( square );
    }
}
