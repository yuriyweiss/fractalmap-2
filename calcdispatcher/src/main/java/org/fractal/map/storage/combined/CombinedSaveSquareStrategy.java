package org.fractal.map.storage.combined;

import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.conf.Configuration;
import org.fractal.map.model.Square;
import org.fractal.map.storage.disk.SaveSquareToDiskStrategy;
import org.fractal.map.storage.mysql.SaveSquareInfoToMySQLStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CombinedSaveSquareStrategy implements SaveSquareStrategy {

    private SaveSquareInfoToMySQLStrategy saveSquareInfoToMySQLStrategy;
    private final String rootDir;

    public CombinedSaveSquareStrategy() {
        this.rootDir = Configuration.getStorageDiskRootDir();
    }

    public CombinedSaveSquareStrategy( String rootDir ) {
        this.rootDir = rootDir;
    }

    @Autowired
    public void setSaveSquareInfoToMySQLStrategy( SaveSquareInfoToMySQLStrategy saveSquareInfoToMySQLStrategy ) {
        this.saveSquareInfoToMySQLStrategy = saveSquareInfoToMySQLStrategy;
    }

    @Override
    public void save( Square square, int[][] points ) throws IOException {
        saveSquareInfoToMySQLStrategy.save( square, points );
        new SaveSquareToDiskStrategy( rootDir ).save( square, points );
    }

    @Override
    public void save( Square square, byte[] squareBody ) throws IOException {
        saveSquareInfoToMySQLStrategy.save( square, squareBody );
        new SaveSquareToDiskStrategy( rootDir ).save( square, squareBody );
    }
}
