package org.fractal.map.storage.mysql;

import org.fractal.map.message.response.TextObjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TextObjectsFromMysqlLoader {

    private static final String SELECT_TEXT_OBJECTS_SQL =
            "select `re`, `im`, `text` from `text_object` " +
                    "where `min_layer_index` <= ? and `re` > ? and `re` < ? and `im` > ? and `im` < ?";

    private JdbcTemplate mysqlJdbcTemplate;

    @Autowired
    public TextObjectsFromMysqlLoader( JdbcTemplate mysqlJdbcTemplate ) {
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    public List<TextObjectInfo> load( int currentLayerIndex, double leftRe, double rightRe, double bottomIm,
            double topIm ) {
        return mysqlJdbcTemplate.query( SELECT_TEXT_OBJECTS_SQL,
                new Object[]{ currentLayerIndex, leftRe, rightRe, bottomIm, topIm },
                ( rs, rowNum ) -> new TextObjectInfo(
                        rs.getDouble( "re" ),
                        rs.getDouble( "im" ),
                        rs.getString( "text" ) ) );
    }
}
