package com.example.healthcare;

import java.sql.Connection;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DatabaseHikariCPUtilsTest {

	@After
    public void tearDown() {
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
