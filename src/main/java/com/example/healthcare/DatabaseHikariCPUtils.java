package com.example.healthcare;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHikariCPUtils {

	private static final HikariDataSource hikariDataSource = hikariDataSource();

	public static Connection getConnection() throws SQLException {
		return hikariDataSource.getConnection();
	}

	private static HikariDataSource hikariDataSource() {
		String jdbcUrl = System.getProperty("jdbcUrl");
		HikariConfig hikariConfig = hikariConfig();
		if (jdbcUrl != null && !jdbcUrl.isEmpty()) {
			hikariConfig.setJdbcUrl(jdbcUrl);
		}
		return new HikariDataSource(hikariConfig);
	}

	private static HikariConfig hikariConfig() {
		Properties properties = PropertiesReaderUtils.readProperties("hikari.properties");
		return new HikariConfig(properties);
	}

	public static void close() {
		if (hikariDataSource != null) {
			hikariDataSource.close();
		}
	}

}
