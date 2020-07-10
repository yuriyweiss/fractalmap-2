package org.fractal.map.storage.combined;

import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.conf.Configuration;
import org.fractal.map.launcher.ApplicationContextHolder;
import org.fractal.map.model.Square;
import org.fractal.map.storage.disk.SaveSquareToDiskStrategy;
import org.fractal.map.storage.mysql.SaveSquareInfoToMysqlStrategy;

import java.io.IOException;

public class CombinedSaveSquareStrategy implements SaveSquareStrategy {

    private final SaveSquareInfoToMysqlStrategy saveSquareInfoToMySQLStrategy;
    private final String rootDir;

    public CombinedSaveSquareStrategy() {
        this( Configuration.getStorageDiskRootDir() );
    }

    public CombinedSaveSquareStrategy( String rootDir ) {
        this.rootDir = rootDir;
        this.saveSquareInfoToMySQLStrategy =
                ApplicationContextHolder.getApplicationContext().getBean( SaveSquareInfoToMysqlStrategy.class );
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
