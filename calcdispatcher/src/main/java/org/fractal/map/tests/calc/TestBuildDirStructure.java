package org.fractal.map.tests.calc;

import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.conf.Configuration;
import org.fractal.map.model.Layer;
import org.fractal.map.model.LayerRegistry;
import org.fractal.map.model.Square;
import org.fractal.map.storage.disk.FileSystemHelper;

import java.io.File;

public class TestBuildDirStructure {

    public static void main( String[] args ) throws Exception {
        System.setProperty( "log4j.configurationFile", "conf/log4j2-calcdisp.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/calcdisp.conf" );

        // layerIndex = 6
        Layer layer = LayerRegistry.getLayerByIndex( 6 );

        // layer y = 1023
        // layer x = 1022
        // square index = 1023 * 2048 + 1022 = 2096126
        // width = 3 / 2048 = 0.00146484375
        // leftRe = -2 + 1022 * 0.00146484375 = -0.5029296875
        // topIm = 1.5 - 1023 * 0.00146484375 = 0.00146484375
        // dirStructure = {"layer_6", "002", "096"}
        Square square1 = new Square( layer, -0.5029296875, 0.00146484375 );
        FileSystemHelper fsHelper1 =
                new FileSystemHelper( square1, Configuration.getStorageDiskRootDir() );
        fsHelper1.createDirectories();

        String testDirName = Configuration.getStorageDiskRootDir();
        testDirName += "/layer_6";
        System.out.println( getTestResultString( new File( testDirName ).exists() )
                + " test dir exists [" + testDirName + "]" );
        testDirName += "/002";
        System.out.println( getTestResultString( new File( testDirName ).exists() )
                + " test dir exists [" + testDirName + "]" );
        testDirName += "/096";
        System.out.println( getTestResultString( new File( testDirName ).exists() )
                + " test dir exists [" + testDirName + "]" );
        testDirName += "/126";
        System.out.println( getTestResultString( !( new File( testDirName ).exists() ) )
                + " test dir NOT exists [" + testDirName + "]" );

        String testFileName =
                Configuration.getStorageDiskRootDir() + "/layer_6/002/096/2096126_x_1022_y_1023.dat";
        System.out.println( getTestResultString( testFileName.equals( fsHelper1.getFileName() ) )
                + " test file name [" + testFileName + "]" );

        // layer y = 0
        // layer x = 980
        // squareIndex = 980
        // leftRe = -2 + 980 * 0.00146484375 = -0.564453125
        // topIm = 1.5
        // dirStructure = {"layer_6", "000", "000"}
        Square square2 = new Square( layer, -0.564453125, 1.5 );
        FileSystemHelper fsHelper2 =
                new FileSystemHelper( square2, Configuration.getStorageDiskRootDir() );
        fsHelper2.createDirectories();

        testDirName = Configuration.getStorageDiskRootDir();
        testDirName += "/layer_6";
        System.out.println( getTestResultString( new File( testDirName ).exists() )
                + " test dir exists [" + testDirName + "]" );
        testDirName += "/000";
        System.out.println( getTestResultString( new File( testDirName ).exists() )
                + " test dir exists [" + testDirName + "]" );
        testDirName += "/000";
        System.out.println( getTestResultString( new File( testDirName ).exists() )
                + " test dir exists [" + testDirName + "]" );
        testDirName += "/980";
        System.out.println( getTestResultString( !( new File( testDirName ).exists() ) )
                + " test dir NOT exists [" + testDirName + "]" );

        testFileName = Configuration.getStorageDiskRootDir() + "/layer_6/000/000/980_x_980_y_0.dat";
        System.out.println( getTestResultString( testFileName.equals( fsHelper2.getFileName() ) )
                + " test file name [" + testFileName + "]" );
    }

    private static String getTestResultString( boolean success ) {
        return success ? "OK" : "FAIL";
    }
}
