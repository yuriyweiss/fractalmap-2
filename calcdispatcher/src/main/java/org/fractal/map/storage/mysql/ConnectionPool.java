package org.fractal.map.storage.mysql;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.fractal.map.conf.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool {

    private PoolingDataSource<PoolableConnection> dataSource;
    private ObjectPool<PoolableConnection> connections;

    private static ConnectionPool instance = null;

    public static ConnectionPool getInstance() throws ReflectiveOperationException {
        if ( instance == null ) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private ConnectionPool() throws ReflectiveOperationException {
        Class.forName( "org.mariadb.jdbc.Driver" ).newInstance(); // NOSONAR

        String connectionString = Configuration.getStorageMySQLConnectionString();
        Properties props = new Properties();
        props.setProperty( "user", Configuration.getStorageMySQLConnectionUser() );
        props.setProperty( "password", Configuration.getStorageMySQLConnectionPassword() );
        ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory( connectionString, props );

        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory( connectionFactory, null );
        poolableConnectionFactory.setDefaultAutoCommit( false );

        connections = new GenericObjectPool<>( poolableConnectionFactory );
        ( ( GenericObjectPool<PoolableConnection> ) connections )
                .setMaxTotal( Configuration.getStorageMySQLConnectionPoolSize() );
        poolableConnectionFactory.setPool( connections );

        dataSource = new PoolingDataSource<>( connections );
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        connections.close();
    }
}
