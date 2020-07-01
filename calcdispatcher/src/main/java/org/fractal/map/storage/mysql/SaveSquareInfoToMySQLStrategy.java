package org.fractal.map.storage.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.model.Square;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveSquareInfoToMySQLStrategy implements SaveSquareStrategy {

    private static final Logger logger = LogManager.getLogger();

    private Connection conn;

    public SaveSquareInfoToMySQLStrategy() throws Exception {
        this.conn = ConnectionPool.getInstance().getConnection();
    }

    @Override
    public void save( Square square, int[][] points ) throws Exception {
        saveToDB( square );
    }

    @Override
    public void save( Square square, byte[] body ) throws Exception {
        saveToDB( square );
    }

    private void saveToDB( Square square ) throws Exception {
        saveSquareInfoToDB( square.getLayer().getLayerIndex(), square.getLeftRe(),
                square.getTopIm(), square.getIterations() );
        try {
            if ( conn != null ) {
                conn.close();
                conn = null;
            }
        } catch ( SQLException e ) {
            logger.warn( "connection close error" );
            logger.debug( "error stack: ", e );
        }
    }

    private void saveSquareInfoToDB( int layerIndex, double leftRe, double topIm, int iterations )
            throws Exception {
        PreparedStatement stmt = null;
        try {
            // Ignore duplicate row error.
            // If square is loaded, but square body not found,
            // then SAVE with full calculation will be performed,
            // and will fire square info save too.
            String sql =
                    "insert ignore into `square`(`layer_index`, `left_re`, `top_im`, `iterations`) "
                            + "values(?, ?, ?, ?)";
            stmt = conn.prepareStatement( sql );
            stmt.setInt( 1, layerIndex );
            stmt.setDouble( 2, leftRe );
            stmt.setDouble( 3, topIm );
            stmt.setInt( 4, iterations );
            stmt.executeUpdate();
            conn.commit();
        } catch ( SQLException e ) {
            logger.error( "save square to DB error" );
            logger.debug( "error stack: ", e );
            conn.rollback();
        } finally {
            if ( stmt != null ) {
                stmt.close();
                stmt = null;
            }
        }
    }
}
