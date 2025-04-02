package com.example.healthcare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private static final String JDBC_URL = "jdbc:h2:~/testdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS patients (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "age INT NOT NULL, " +
                "diagnosis VARCHAR(255) NOT NULL" +
                ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearDatabase() {
        String dropTableSQL = "DROP TABLE IF EXISTS patients";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(dropTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}