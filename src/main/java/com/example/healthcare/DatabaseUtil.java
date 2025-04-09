package com.example.healthcare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DatabaseUtil {
    private static final String JDBC_URL = "jdbc:p6spy:oracle:thin:@//%s:1521/xe";
    private static final String JDBC_USER = "system";
    private static final String JDBC_PASSWORD = "oracle";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getJdbcUrl(), JDBC_USER, JDBC_PASSWORD);
    }

    private static String getJdbcUrl() {
        Map<String, String> getenv = System.getenv();
        String dbHost = getenv.getOrDefault("DB_HOST", "localhost");
        return String.format(JDBC_URL,dbHost);
    }

    public static void truncateTable() {
        String truncateTableSQL = "TRUNCATE TABLE dem1.patients";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(truncateTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}