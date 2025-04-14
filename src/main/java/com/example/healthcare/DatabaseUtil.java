package com.example.healthcare;

import com.zaxxer.hikari.HikariDataSource;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import oracle.jdbc.datasource.impl.OracleConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DatabaseUtil {
//    private static final String JDBC_URL = "jdbc:p6spy:oracle:thin:@//%s:1521/xe";
    private static final String JDBC_URL = "jdbc:oracle:thin:@//%s:1521/xe";
    private static final String JDBC_USER = "system";
    private static final String JDBC_PASSWORD = "oracle";
    private static OracleConnectionPoolDataSource ds = OracleDS();
    private static HikariDataSource hds = hikari();

    private static HikariDataSource hikari() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(getJdbcUrl());
        hikariDataSource.setUsername(JDBC_USER);
        hikariDataSource.setPassword(JDBC_PASSWORD);
        return hikariDataSource;
    }

    @WithSpan(value = "get_connection")
    public static Connection getConnection1() throws SQLException {
        return DriverManager.getConnection(getJdbcUrl(), JDBC_USER, JDBC_PASSWORD);
    }

    @WithSpan(value = "get_connection_pool")
    public static Connection getConnection() throws SQLException {
        return hds.getConnection();
    }

    @WithSpan(value = "get_connection_pool")
    public static Connection getConnection2() throws SQLException {
        ds.setURL(getJdbcUrl());
        ds.setUser(JDBC_USER);
        ds.setPassword(JDBC_PASSWORD);
        PooledConnection pc = ds.getPooledConnection();
        return pc.getConnection();
    }

    private static OracleConnectionPoolDataSource OracleDS()  {
        try {
            return new OracleConnectionPoolDataSource();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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