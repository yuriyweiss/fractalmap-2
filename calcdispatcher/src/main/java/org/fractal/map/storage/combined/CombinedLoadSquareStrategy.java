package org.fractal.map.storage.combined;

import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.conf.Configuration;
import org.fractal.map.model.Square;
import org.fractal.map.storage.disk.LoadSquareFromDiskStrategy;
import org.fractal.map.storage.mysql.LoadSquareFromMySQLStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CombinedLoadSquareStrategy implements LoadSquareStrategy {

    private LoadSquareFromMySQLStrategy loadSquareFromMySQLStrategy;
    private final String rootDir;

    public CombinedLoadSquareStrategy() {
        this.rootDir = Configuration.getStorageDiskRootDir();
    }

    public CombinedLoadSquareStrategy( String rootDir ) {
        this.rootDir = rootDir;
    }

    @Autowired
    public void setLoadSquareFromMySQLStrategy( LoadSquareFromMySQLStrategy loadSquareFromMySQLStrategy ) {
        this.loadSquareFromMySQLStrategy = loadSquareFromMySQLStrategy;
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
