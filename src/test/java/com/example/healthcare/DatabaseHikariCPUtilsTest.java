package com.example.healthcare;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseHikariCPUtilsTest {

	@AfterAll
	static void tearDown() {
		DatabaseHikariCPUtils.close();
	}

	@Test
	public void testConnection() throws Exception {
		try (Connection connection = DatabaseHikariCPUtils.getConnection()) {
			assertNotNull(connection);
			assertTrue(connection.isValid(3));
		}
	}

}
