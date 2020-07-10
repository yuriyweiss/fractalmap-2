package org.fractal.map.storage.mysql;

import org.fractal.map.calc.LoadSquareStrategy;
import org.fractal.map.model.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LoadSquareFromMysqlStrategy implements LoadSquareStrategy {

    private static final String SELECT_SQUARE_SQL = "select `iterations` from square " +
            "where `layer_index` = ? and `left_re` = ? and `top_im` = ?";

    private JdbcTemplate mysqlJdbcTemplate;

    @Autowired
    public LoadSquareFromMysqlStrategy( JdbcTemplate mysqlJdbcTemplate ) {
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public Square loadSquare( final int layerIndex, final double leftRe, final double topIm ) {
        Square square;
        try {
            square = mysqlJdbcTemplate.queryForObject( SELECT_SQUARE_SQL, new Object[]{ layerIndex, leftRe, topIm },
                    ( rs, rowNum ) -> {
                        Square result = new Square( layerIndex, leftRe, topIm );
                        result.setIterations( rs.getInt( "iterations" ) );
                        return result;
                    } );
        } catch ( DataAccessException e ) {
            square = null;
        }
        return square;
    }

    @Override
    public byte[] loadSquareBody( Square square ) {
        throw new IllegalAccessError( "Body is not stored in MySQL." );
    }
}
