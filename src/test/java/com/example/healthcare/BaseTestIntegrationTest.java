package com.example.healthcare;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

@Testcontainers
public class BaseTestIntegrationTest {

	public static final String DOCKER_IMAGE_NAME = "gvenzl/oracle-free:23.6-slim-faststart";
	public static final String JDBC_URL_PATTERN = "jdbc:oracle:thin:@localhost:%d/%s";
	public static final int PORT = 1521;

	protected static OracleContainer oracleContainer = new OracleContainer(DOCKER_IMAGE_NAME)
			.withExposedPorts(PORT);

	@BeforeAll
	public static void setUp() {
		oracleContainer.start();
	}

	@AfterAll
	public static void tearDown() {
		if (oracleContainer != null) {
			oracleContainer.stop();
		}
	}

}
