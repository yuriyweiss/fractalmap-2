package org.fractal.map.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.model.Square;

public class LoadSquareFromMySQLStrategy implements LoadSquareStrategy {

    @Override
    public Square loadSquare( int layerIndex, double leftRe, double topIm ) throws Exception {
        Connection conn = ConnectionPool.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "select `iterations` from square " + "where `layer_index` = ? "
                            + "and `left_re` = ? " + "and `top_im` = ?";
            stmt = conn.prepareStatement( sql );
            stmt.setInt( 1, layerIndex );
            stmt.setDouble( 2, leftRe );
            stmt.setDouble( 3, topIm );
            rs = stmt.executeQuery();
            if ( rs.next() ) {
                int iterations = rs.getInt( 1 );
                Square result = new Square( layerIndex, leftRe, topIm );
                result.setIterations( iterations );
                return result;
            } else {
                return null;
            }
        } finally {
            if ( rs != null ) {
                rs.close();
                rs = null;
            }
            if ( stmt != null ) {
                stmt.close();
                stmt = null;
            }
            if ( conn != null ) {
                conn.close();
                conn = null;
            }
        }
    }

    @Override
    public byte[] loadSquareBody( Square square ) throws Exception {
        // Body is not stored in MySQL.
        return null;
    }

}
