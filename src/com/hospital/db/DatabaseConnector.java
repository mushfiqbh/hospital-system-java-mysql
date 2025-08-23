package com.hospital.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles the connection to the MySQL database.
 * Delegates helper tasks like user prompts and setup checks to DatabaseHelper.
 */
public class DatabaseConnector {
    private static final String DB_NAME = "project_hospital_db_m3";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "";
    private static String DB_PORT = "3306";
    private static String DB_URL = buildUrl();

    private static String buildUrl() {
        return "jdbc:mysql://localhost:" + DB_PORT + "/" + DB_NAME;
    }

    public static Connection getConnection() {
        while (true) {
            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                return conn;

            } catch (SQLException e) {
                try {
                    handleSQLException(e);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "❌ " + ex.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    private static void handleSQLException(SQLException e) throws SQLException {
        String message = e.getMessage().toLowerCase();

        if (message.contains("unknown database")) {
            DatabaseException.missingDatabase(DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);

        } else if (message.contains("communications link failure") || message.contains("connect")) {
            DB_PORT = DatabaseException.promptForPort();
            DB_URL = buildUrl();

        } else if (message.contains("access denied") || message.contains("authentication")) {
            throw new SQLException("Access denied. Please Fix Your Xampp and MySQL issue.");

        } else {
            throw new SQLException("Unexpected error: " + e.getMessage(), e);
        }
    }
}