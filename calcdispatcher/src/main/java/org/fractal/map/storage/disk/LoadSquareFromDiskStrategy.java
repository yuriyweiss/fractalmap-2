package org.fractal.map.storage.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.Constants;
import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.conf.Configuration;
import org.fractal.map.model.Square;

public class LoadSquareFromDiskStrategy implements LoadSquareStrategy {

    private static final Logger logger = LogManager.getLogger();

    private final String rootDir;

    public LoadSquareFromDiskStrategy() {
        this.rootDir = Configuration.getStorageDiskRootDir();
    }

    public LoadSquareFromDiskStrategy( String rootDir ) {
        this.rootDir = rootDir;
    }

    @Override
    public Square loadSquare( int layerIndex, double leftRe, double topIm ) throws Exception {
        // Square info will not be stored in file system.
        return null;
    }

    @Override
    public byte[] loadSquareBody( Square square ) throws Exception {
        if ( square.getIterations() != Constants.ITERATIONS_DIFFER ) return null;

        FileSystemHelper fsHelper = new FileSystemHelper( square, rootDir );
        logger.debug( "loading file: " + fsHelper.getFileName() );
        File squareFile = new File( fsHelper.getFileName() );
        if ( !squareFile.exists() ) throw new FileNotFoundException();

        FileInputStream fis = new FileInputStream( squareFile );
        ByteBuffer inputBuffer = ByteBuffer.allocate( Configuration.getStorageDiskBufferSize() );
        try {
            byte[] buffer = new byte[8096];
            int bytesRead = fis.read( buffer );
            while ( bytesRead != -1 ) {
                inputBuffer.put( buffer, 0, bytesRead );
                bytesRead = fis.read( buffer );
            }
        } finally {
            fis.close();
        }

        inputBuffer.flip();
        byte[] result = new byte[inputBuffer.limit()];
        inputBuffer.get( result );
        return result;
    }
}
