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
    public static void runMigrations() {
        String url = "jdbc:sqlite:hospital.db";
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            String sql = DatabaseSeed.getResetDatabaseQuery();

            String[] individualSqlStatements = sql.split(";");

            for (String statement : individualSqlStatements) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }

            JOptionPane.showMessageDialog(null, "Database migration completed successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Migration failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}