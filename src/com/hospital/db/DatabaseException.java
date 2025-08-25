package com.hospital.db;

import javax.swing.*;
import java.sql.*;

/**
 * Provides helper methods for database interactions. For SQLite, this is
 * simplified to just checking for table existence.
 */
public class DatabaseException {
    public static boolean allTablesExist(Connection conn) {
        String[] tables = { "doctors", "patients", "appointments", "billings" };
        try {
            DatabaseMetaData meta = conn.getMetaData();
            for (String table : tables) {
                try (ResultSet rs = meta.getTables(null, null, table, null)) {
                    if (!rs.next()) {
                        return false;
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking tables: " + e.getMessage());
            return false;
        }
    }
}