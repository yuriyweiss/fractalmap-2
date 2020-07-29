package org.fractal.map.storage.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TextObjectToMysqlSaver {

    private static final Logger logger = LogManager.getLogger();

    private static final double PROXIMITY_DELTA = 1E-7;

    private static final String SELECT_NEAR_COUNT_SQL =
            "select count(*) as `near_count` from `text_object` " +
                    "where `re` > ? and `re` < ? and `im` > ? and `im` < ?";

    private static final String INSERT_TEXT_OBJECT_SQL =
            "insert ignore into `text_object`(`min_layer_index`, `re`, `im`, `text`) " +
                    "values(?, ?, ?, ?)";

    private JdbcTemplate mysqlJdbcTemplate;

    @Autowired
    public TextObjectToMysqlSaver( JdbcTemplate mysqlJdbcTemplate ) {
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    public void save( int layerIndex, double re, double im, String text ) {
        // check near objects present
        Integer nearCount = mysqlJdbcTemplate.queryForObject( SELECT_NEAR_COUNT_SQL,
                new Object[]{ re - PROXIMITY_DELTA, re + PROXIMITY_DELTA, im - PROXIMITY_DELTA,
                        im + PROXIMITY_DELTA },
                Integer.class );
        if ( nearCount > 0 ) {
            logger.info( "SAVE FAILURE some objects near saving point [re: {}, im: {}] found", re, im );
            return;
        }
        // save object
        mysqlJdbcTemplate.update( INSERT_TEXT_OBJECT_SQL, layerIndex, re, im, text );
    }
}
