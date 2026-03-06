package com.oceanview.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection singleton class
 * Manages database connections for the application
 */
public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    
    // Database configuration - should be loaded from config file in production
    private static final String DB_URL = "jdbc:mysql://localhost:3306/oceanview_resort";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private DBConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    public static DBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
