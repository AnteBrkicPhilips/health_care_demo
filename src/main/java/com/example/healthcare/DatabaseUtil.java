package com.example.healthcare;

import com.zaxxer.hikari.HikariDataSource;
//import epam.ConnectionErrorMetrics;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import oracle.jdbc.datasource.impl.OracleConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseUtil {
//    private static final String JDBC_URL = "jdbc:p6spy:oracle:thin:@//%s:1521/xe";
    private static final String JDBC_URL = "jdbc:oracle:thin:@//%s:1521/xe";
    private static final String JDBC_USER = "system";
    private static final String JDBC_PASSWORD = "oracle";
    private static OracleConnectionPoolDataSource ds = OracleDS();
    private static HikariDataSource hds = hikari();
    private static final String JDBC_PROPERTY_FILE = "jdbc.properties";
    private static final String JDBC_URL_PROPERTY_NAME = "jdbcUrl";
    private static AtomicInteger activeConnections = new AtomicInteger(0);
//    private static ConnectionErrorMetrics connectionErrorMetrics = new ConnectionErrorMetrics();

    private static HikariDataSource hikari() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(getJdbcUrl());
        hikariDataSource.setUsername(JDBC_USER);
        hikariDataSource.setPassword(JDBC_PASSWORD);
        return hikariDataSource;
    }

    @WithSpan(value = "get_connection")
    public static Connection getConnection(){
        return getConnection(getJdbcUrl());
    }

    public static Connection getConnection(String url) {
        try {
            Properties jdbcProperties = PropertiesReaderUtils.readProperties(JDBC_PROPERTY_FILE);
            String jdbcUrl = jdbcProperties.getProperty(JDBC_URL_PROPERTY_NAME, url);
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcProperties);
            activeConnections.incrementAndGet();
            return connection;
        }
        catch (SQLException e) {
//            connectionErrorMetrics.getErrorCounter().add(1);
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
            activeConnections.decrementAndGet();
        }
    }

    public static int getActiveConnections() {
        return activeConnections.get();
    }

    @WithSpan(value = "get_connection_pool")
    public static Connection getConnectionByHikari() throws SQLException {
        return hds.getConnection();
    }

    @WithSpan(value = "get_connection_pool")
    public static Connection getConnectionByJdbcPool() throws SQLException {
        return getConnectionByJdbcPool(getJdbcUrl());
    }

    @WithSpan(value = "get_connection_pool")
    public static Connection getConnectionByJdbcPool(String url) throws SQLException {
        Properties jdbcProperties = PropertiesReaderUtils.readProperties(JDBC_PROPERTY_FILE);
        String jdbcUrl = jdbcProperties.getProperty(JDBC_URL_PROPERTY_NAME, url);
        ds.setURL(jdbcUrl);
        ds.setConnectionProperties(jdbcProperties);
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