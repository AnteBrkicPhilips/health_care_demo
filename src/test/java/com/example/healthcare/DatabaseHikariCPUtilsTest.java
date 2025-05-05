package com.example.healthcare;

import java.sql.Connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DatabaseHikariCPUtilsTest {

	@AfterAll
    public static void tearDown() {
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
