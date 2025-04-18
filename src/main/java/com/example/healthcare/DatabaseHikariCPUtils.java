package com.example.healthcare;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHikariCPUtils {

	private static HikariDataSource hikariDataSource = hikariDataSource();

	public static Connection getConnection() throws SQLException {
		return hikariDataSource.getConnection();
	}

	private static HikariDataSource hikariDataSource() {
		HikariConfig hikariConfig = hikariConfig();
		return new HikariDataSource(hikariConfig);
	}

	private static HikariConfig hikariConfig() {
		Properties properties = readProperties();
		return new HikariConfig(properties);
	}

	private static Properties readProperties(){
		Properties properties = new Properties();
		try (InputStream input = DatabaseHikariCPUtils.class.getClassLoader().getResourceAsStream("hikari.properties")) {
			properties.load(input);
		}
		catch (IOException e) {
			System.err.println("Unable to load hikari.properties");
		}
		return properties;
	}

	public static void close() {
		if (hikariDataSource != null) {
			hikariDataSource.close();
		}
	}

}
