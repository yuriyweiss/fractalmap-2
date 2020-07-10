package org.fractal.map.launcher;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SpringBeansConfiguration {

    @Bean( "mysqlDataSource" )
    public MariaDbDataSource mysqlDataSource(
            @Value( "${storage.mysql.connection.url}" ) String connectionUrl,
            @Value( "${storage.mysql.connection.username}" ) String username,
            @Value( "${storage.mysql.connection.password}" ) String password ) {
        DataSourceBuilder<MariaDbDataSource> dataSourceBuilder =
                ( DataSourceBuilder<MariaDbDataSource> ) DataSourceBuilder.create();
        dataSourceBuilder.driverClassName( "org.mariadb.jdbc.Driver" );
        dataSourceBuilder.url( connectionUrl );
        dataSourceBuilder.username( username );
        dataSourceBuilder.password( password );
        return dataSourceBuilder.build();
    }

    @Bean( "mysqlJdbcTemplate" )
    public JdbcTemplate mysqlJdbcTemplate( MariaDbDataSource mysqlDataSource ) {
        return new JdbcTemplate( mysqlDataSource );
    }
}
