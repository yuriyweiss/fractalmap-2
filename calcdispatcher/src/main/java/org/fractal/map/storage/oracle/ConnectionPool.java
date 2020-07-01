package org.fractal.map.storage.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import org.fractal.map.conf.Configuration;

public class ConnectionPool
{
	private PoolingDataSource<PoolableConnection> dataSource;
	private ObjectPool<PoolableConnection> connectionPool;

	private static ConnectionPool instance = null;

	public static ConnectionPool getInstance() throws Exception
	{
		if (instance == null)
		{
			instance = new ConnectionPool();
		}
		return instance;
	}

	private ConnectionPool() throws Exception
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");

		String connectionString = Configuration.getStorageOracleConnectionString();
		Properties props = new Properties();
		props.setProperty("user", Configuration.getStorageOracleConnectionUser());
		props.setProperty("password", Configuration.getStorageOracleConnectionPassword());
		ConnectionFactory connectionFactory =
		    new DriverManagerConnectionFactory(connectionString, props);

		PoolableConnectionFactory poolableConnectionFactory =
		    new PoolableConnectionFactory(connectionFactory, null);
		poolableConnectionFactory.setDefaultAutoCommit(false);

		connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
		((GenericObjectPool<PoolableConnection>) connectionPool).setMaxTotal(Configuration.getStorageOracleConnectionPoolSize());
		poolableConnectionFactory.setPool(connectionPool);

		this.dataSource = new PoolingDataSource<>(connectionPool);
	}

	public Connection getConnection() throws SQLException
	{
		// logger.debug(dataSource.getConnection().unwrap(OracleConnection.class));
		return dataSource.getConnection();
	}

	public void close()
	{
		connectionPool.close();
	}
}
