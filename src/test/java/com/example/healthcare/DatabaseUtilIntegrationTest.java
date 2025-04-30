package com.example.healthcare;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Testcontainers
public class DatabaseUtilIntegrationTest {

	private static final String DOCKER_IMAGE_NAME = "gvenzl/oracle-free:23.6-slim-faststart";
	private static final String JDBC_URL_PATTERN = "jdbc:oracle:thin:@localhost:%d/%s";
	private static final int PORT = 1521;

	static OracleContainer oracleContainer = new OracleContainer(DOCKER_IMAGE_NAME)
			.withExposedPorts(PORT);

	@Before
	public void setUp() {
		System.out.println("Start Container");
		oracleContainer.start();
	}

	@After
	public void tearDown() {
		if (oracleContainer != null) {
			oracleContainer.stop();
		}
	}

	@Test
	public void testGetConnection() throws SQLException {
		Integer mappedPort = oracleContainer.getMappedPort(PORT);
		String jdbcUrl = String.format(JDBC_URL_PATTERN, mappedPort, oracleContainer.getDatabaseName());
		try (Connection connection = DatabaseUtil.getConnection(jdbcUrl)) {
			assertNotNull(connection);
			assertTrue(connection.isValid(3));
		}
	}

}
