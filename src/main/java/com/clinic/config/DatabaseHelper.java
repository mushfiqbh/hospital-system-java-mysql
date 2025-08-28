package com.clinic.config;

import com.clinic.dao.UserDao;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Manages the connection to the SQLite database, table creation, and initial data seeding.
 */
public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:clinic_management.db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static void createDefaultAdmin() {
        UserDao userDao = new UserDao();
        if (userDao.getUserByUsername("admin") == null) {
            String sql = "INSERT INTO users(username, password, role, name, contact) VALUES('admin', 'admin', 'admin', 'Default Admin', 'N/A')";
            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Default admin user (admin/admin) created successfully.");
            } catch (SQLException e) {
                System.err.println("SQL Error while creating default admin user: " + e.getMessage());
            }
        }
    }

    public static void createTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            for (String sql : DatabaseQuery.getCreateTableSQLs()) {
                stmt.execute(sql);
            }
            System.out.println("All tables created or already exist.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void seedDatabase() {
        String checkSql = "SELECT COUNT(*) FROM doctors";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                int response = JOptionPane.showConfirmDialog(null, "The database is empty. Would you like to add sample data?", "Database Seeding", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    System.out.println("User chose to seed data. Inserting sample data...");
                    for (String sql : DatabaseQuery.getSeedData()) {
                        stmt.execute(sql);
                    }
                    System.out.println("Sample data seeded successfully.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during database seeding: " + e.getMessage());
        }
    }
}
