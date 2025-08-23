package com.hospital.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides helper methods for database interactions, such as user prompts,
 * setup checks and database creation.
 */
public class DatabaseException {
    public static boolean createDatabase(String dbPort, String dbName, String dbUser, String dbPassword) {
        String url = "jdbc:mysql://localhost:" + dbPort + "/?useSSL=false";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE " + dbName);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error creating database: " + ex.getMessage());
            return false;
        }
    }

    public static void missingDatabase(String dbPort, String dbName, String dbUser, String dbPassword)
            throws SQLException {
        int createDb = JOptionPane.showConfirmDialog(null,
                "Database '" + dbName + "' not found.\nDo you want to create it automatically?",
                "Database Not Found", JOptionPane.YES_NO_OPTION);

        if (createDb == JOptionPane.YES_OPTION) {
            if (createDatabase(dbPort, dbName, dbUser, dbPassword)) {
                JOptionPane.showMessageDialog(null,
                        "Database '" + dbName + "' created successfully.\nRunning migrations...");
                DatabaseMigration.runMigrations(dbPort, dbName, dbUser, dbPassword);

            } else {
                throw new SQLException("Failed to create database. Check MySQL permissions.");
            }
        } else {
            throw new SQLException("Database not found. User declined auto-create.");
        }
    }

    public static String promptForPort() {
        String port = JOptionPane.showInputDialog(
                null,
                "Please check if MySQL is running in XAMPP and enter the MySQL port:",
                "3306");
        if (port != null && !port.trim().isEmpty()) {
            return port.trim();
        }
        System.exit(0);
        return "3306";
    }
}