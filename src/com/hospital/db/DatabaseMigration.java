package com.hospital.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the database migration process, including table creation and initial
 * data seeding.
 */
public class DatabaseMigration {
    public static void runMigrations(String dbPort, String dbName, String dbUser, String dbPassword) {
        String url = "jdbc:mysql://localhost:" + dbPort + "/" + dbName + "?allowMultiQueries=true";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
                Statement stmt = conn.createStatement()) {

            String sql = DatabaseSeed.getResetDatabaseQuery();
            stmt.execute(sql);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Migration failed: " + e.getMessage());
            System.exit(1);
        }
    }
}
