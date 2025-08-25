package com.hospital.db;

import javax.swing.*;
import java.sql.*;

public class DatabaseConnector {
    private static final String DB_NAME = "project_hospital.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    public static Connection getConnection() {
        while (true) {
            try {
                Connection conn = DriverManager.getConnection(DB_URL);

                if (!DatabaseException.allTablesExist(conn)) {
                    throw new SQLException("Required database tables are missing.");
                }
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

        if (message.contains("required database tables are missing")) {
            int migrate = JOptionPane.showConfirmDialog(null,
                    "Database tables are missing. Create and seed them now?",
                    "Missing Tables", JOptionPane.YES_NO_OPTION);

            if (migrate == JOptionPane.YES_OPTION) {
                DatabaseMigration.runMigrations();
            } else {
                throw new SQLException("Required tables missing. Migration declined by user.");
            }
        } else {
            throw new SQLException("Unexpected database error: " + e.getMessage(), e);
        }
    }
}