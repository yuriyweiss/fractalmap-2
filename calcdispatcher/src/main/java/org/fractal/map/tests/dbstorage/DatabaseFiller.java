package org.fractal.map.tests.dbstorage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.fractal.map.conf.Configuration;
import org.fractal.map.monitor.MonitorThread;
import org.fractal.map.storage.oracle.ConnectionPool;
import org.fractal.map.tests.dbstorage.calc.FillDbTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseFiller {

    private static final Logger logger = LogManager.getLogger();

    public static void main( String[] args ) throws Exception {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!
        // FIXME obsolete, not working
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!
        System.setProperty( "log4j.configurationFile", "conf/log4j2-load-test.xml" );
        LoggerContext.getContext( false ).reconfigure();
        Configuration.load( "conf/load-test.conf" );

        MonitorThread monitorThread = new MonitorThread();

        Connection conn = null;
        ExecutorService executor = Executors.newFixedThreadPool( 1 );
        try {
            conn = ConnectionPool.getInstance().getConnection();
            conn.setAutoCommit( false );
            clearLayerSquares( conn );
            Future<?> future = executor.submit( new FillDbTask( conn ) );
            monitorThread.start();
            try {
                future.get();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        } finally {
            monitorThread.terminate();
            executor.shutdown();
            if ( conn != null ) {
                conn.close();
            }
            ConnectionPool.getInstance().close();
        }

        logger.info( "application stopped" );
    }

    private static void clearLayerSquares( Connection conn ) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM SQUARE WHERE LAYER_INDEX = ?";
            stmt = conn.prepareStatement( sql );
            stmt.setInt( 1, Configuration.getLayerIndex() );
            stmt.executeUpdate();
            conn.commit();
        } catch ( SQLException e ) {
            logger.error( e.getMessage(), e );
            conn.rollback();
        } finally {
            if ( stmt != null ) {
                stmt.close();
            }
        }
    }
}
