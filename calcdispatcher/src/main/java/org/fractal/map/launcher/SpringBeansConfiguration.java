package org.fractal.map.launcher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SpringBeansConfiguration {

    @Bean( "mysqlDataSource" )
    public DataSource mysqlDataSource(
            @Value( "${storage.mysql.connection.url}" ) String connectionUrl,
            @Value( "${storage.mysql.connection.username}" ) String username,
            @Value( "${storage.mysql.connection.password}" ) String password ) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(); // NOSONAR
        dataSourceBuilder.driverClassName( "org.mariadb.jdbc.Driver" );
        dataSourceBuilder.url( connectionUrl );
        dataSourceBuilder.username( username );
        dataSourceBuilder.password( password );
        return dataSourceBuilder.build();
    }

    @Bean( "mysqlJdbcTemplate" )
    public JdbcTemplate mysqlJdbcTemplate( DataSource mysqlDataSource ) {
        return new JdbcTemplate( mysqlDataSource );
    }
}
