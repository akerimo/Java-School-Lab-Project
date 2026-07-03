package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Update these credentials based on your local SQL Server Management Studio
    // configuration
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=SimpleBankingAppDB;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly load the Microsoft SQL Server JDBC Driver class
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "JDBC Driver not found. Ensure the JAR file is included in your project dependencies.", e);
        }
    }
}