package org.fractal.map.storage.disk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.CalcUtils;
import org.fractal.map.calc.Constants;
import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.conf.Configuration;
import org.fractal.map.model.Square;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveSquareToDiskStrategy implements SaveSquareStrategy {

    private static final Logger logger = LogManager.getLogger();

    private final String rootDir;

    public SaveSquareToDiskStrategy() {
        this.rootDir = Configuration.getStorageDiskRootDir();
    }

    public SaveSquareToDiskStrategy( String rootDir ) {
        this.rootDir = rootDir;
    }

    @Override
    public void save( Square square, int[][] points ) throws IOException {
        if ( square.getIterations() != Constants.ITERATIONS_DIFFER ) return;

        File squareFile = getSquareFile( square );
        FileOutputStream fos = new FileOutputStream( squareFile, false );
        CalcUtils.saveAndCompressResultToStream( fos, points );
    }

    @Override
    public void save( Square square, byte[] squareBody ) throws IOException {
        if ( square.getIterations() != Constants.ITERATIONS_DIFFER ) return;

        File squareFile = getSquareFile( square );
        FileOutputStream fos = new FileOutputStream( squareFile, false );
        CalcUtils.saveSquareBodyToStream( fos, squareBody );
    }

    private File getSquareFile( Square square ) {
        FileSystemHelper fsHelper = new FileSystemHelper( square, rootDir );
        fsHelper.createDirectories();
        String fileName = fsHelper.getFileName();
        logger.debug( "saving square body to file: " + fileName );
        File squareFile = new File( fileName );
        return squareFile;
    }
}
