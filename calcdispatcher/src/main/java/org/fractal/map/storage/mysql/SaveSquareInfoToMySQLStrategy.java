package org.fractal.map.storage.mysql;

import org.fractal.map.calc.SaveSquareStrategy;
import org.fractal.map.model.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SaveSquareInfoToMySQLStrategy implements SaveSquareStrategy {

    private static final String INSERT_SQUARE_SQL =
            "insert ignore into `square`(`layer_index`, `left_re`, `top_im`, `iterations`) " +
                    "values(?, ?, ?, ?)";

    private JdbcTemplate mysqlJdbcTemplate;

    @Autowired
    public SaveSquareInfoToMySQLStrategy( JdbcTemplate mysqlJdbcTemplate ) {
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public void save( Square square, int[][] points ) {
        saveToDB( square );
    }

    @Override
    public void save( Square square, byte[] body ) {
        saveToDB( square );
    }

    private void saveToDB( Square square ) {
        mysqlJdbcTemplate.update( INSERT_SQUARE_SQL, square.getLayer().getLayerIndex(), square.getLeftRe(),
                square.getTopIm(), square.getIterations() );
    }
}
