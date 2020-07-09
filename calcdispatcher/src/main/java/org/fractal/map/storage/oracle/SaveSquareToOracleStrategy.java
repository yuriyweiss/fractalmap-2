package org.fractal.map.storage.oracle;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fractal.map.calc.CalcUtils;
import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.model.Square;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.fractal.map.calc.Constants.ITERATIONS_DIFFER;

public class SaveSquareToOracleStrategy implements SaveSquareStrategy {

    private static final Logger logger = LogManager.getLogger();

    private static final boolean COMPRESS = true;

    private final Connection conn;
    private int[][] points = null;
    private byte[] body = null;

    public SaveSquareToOracleStrategy( Connection conn ) {
        this.conn = conn;
    }

    @Override
    public void save( Square square, int[][] points ) throws SQLException, IOException {
        this.points = points;

        int commonIteration = CalcUtils.getCommonIteration( points );
        saveSquareToDB( square.getLayer().getLayerIndex(), square.getLeftRe(), square.getTopIm(),
                commonIteration );
        if ( commonIteration == ITERATIONS_DIFFER ) {
            saveSquareBodyToDB( square.getLayer().getLayerIndex(), square.getLeftRe(),
                    square.getTopIm(), COMPRESS );
        }
    }

    @Override
    public void save( Square square, byte[] body ) throws SQLException, IOException {
        this.body = body;

        saveSquareToDB( square.getLayer().getLayerIndex(), square.getLeftRe(), square.getTopIm(),
                square.getIterations() );
        if ( square.getIterations() == ITERATIONS_DIFFER ) {
            saveSquareBodyToDB( square.getLayer().getLayerIndex(), square.getLeftRe(),
                    square.getTopIm(), !COMPRESS );
        }
    }

    private void saveSquareToDB( int layerIndex, double leftRe, double topIm, int iterations )
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            String blobInit = ( iterations == ITERATIONS_DIFFER ) ? "empty_blob()" : "NULL";
            String sql =
                    "INSERT INTO SQUARE(LAYER_INDEX, LEFT_RE, TOP_IM, ITERATIONS, SQUARE_BODY) "
                            + "VALUES(?, ?, ?, ?, " + blobInit + ")";
            stmt = conn.prepareStatement( sql );
            stmt.setInt( 1, layerIndex );
            stmt.setDouble( 2, leftRe );
            stmt.setDouble( 3, topIm );
            stmt.setInt( 4, iterations );
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

    private void saveSquareBodyToDB( int layerIndex, double leftRe, double topIm, boolean compress )
            throws SQLException, IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "SELECT SQUARE_BODY " + "FROM SQUARE "
                            + "WHERE LAYER_INDEX = ? AND LEFT_RE = ? AND TOP_IM = ? " + "FOR UPDATE";
            stmt = conn.prepareStatement( sql );
            stmt.setInt( 1, layerIndex );
            stmt.setDouble( 2, leftRe );
            stmt.setDouble( 3, topIm );
            rs = stmt.executeQuery();

            if ( rs.next() ) {
                BLOB blob = ( ( OracleResultSet ) rs ).getBLOB( "SQUARE_BODY" );
                OutputStream os = blob.setBinaryStream( 1L );
                if ( compress ) {
                    CalcUtils.saveAndCompressResultToStream( os, points );
                } else {
                    os.write( body );
                    os.flush();
                    os.close();
                }
            }

            conn.commit();
        } catch ( SQLException e ) {
            logger.error( e.getMessage(), e );
            conn.rollback();
        } finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( stmt != null ) {
                stmt.close();
            }
        }
    }
}
