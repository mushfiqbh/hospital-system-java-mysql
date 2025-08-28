package com.clinic.main;

import com.clinic.config.DatabaseHelper;
import com.clinic.ui.LoginFrame;

import javax.swing.*;

/**
 * Main class to run the Clinic Management System application.
 */
public class Main {

    public static void main(String[] args) {
        // Ensure the database and tables are created before starting the UI
        DatabaseHelper.createTables();

        // Create a default admin user if one doesn't exist
        DatabaseHelper.createDefaultAdmin();

        // Seed the database with initial data
        DatabaseHelper.seedDatabase();

        // Run the Swing GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            new LoginFrame().setVisible(true);
        });
    }
}
