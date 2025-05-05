package com.example.healthcare;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseUtilIntegrationTest extends BaseTestIntegrationTest {

	@Tag("integration")
	@Test
	public void testGetConnection() throws SQLException {
		Integer mappedPort = oracleContainer.getMappedPort(PORT);
		String jdbcUrl = String.format(JDBC_URL_PATTERN, mappedPort, oracleContainer.getDatabaseName());
		try (Connection connection = DatabaseUtil.getConnection(jdbcUrl)) {
			assertNotNull(connection);
			assertTrue(connection.isValid(3));
		}
	}

	@Tag("integration")
	@Test
	public void testGetConnectionByJdbcPool() throws SQLException {
		Integer mappedPort = oracleContainer.getMappedPort(PORT);
		String jdbcUrl = String.format(JDBC_URL_PATTERN, mappedPort, oracleContainer.getDatabaseName());
		try (Connection connection = DatabaseUtil.getConnectionByJdbcPool(jdbcUrl)) {
			assertNotNull(connection);
			assertTrue(connection.isValid(3));
		}
	}

}
