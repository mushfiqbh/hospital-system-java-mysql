package com.hospital.main;

import com.hospital.ui.AppointmentPanel;
import com.hospital.ui.BillingPanel;
import com.hospital.ui.DoctorPanel;
import com.hospital.ui.PatientPanel;

import javax.swing.*;
import java.awt.*;

public class HospitalManagementSystem extends JFrame {

    public HospitalManagementSystem() {
        setTitle("Hospital Management System");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window


        JTabbedPane tabbedPane = new JTabbedPane();

        // Add different management panels as tabs
        tabbedPane.addTab("Doctors", new DoctorPanel());
        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Appointments", new AppointmentPanel());
        tabbedPane.addTab("Billings", new BillingPanel());

        add(tabbedPane);
    }

    public static void main(String[] args) {
        // Set a modern look and feel and customize UI component styles
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Font newFont = new Font("SansSerif", Font.PLAIN, 16);
            Font boldFont = new Font("SansSerif", Font.BOLD, 16);

            // Apply custom fonts to various Swing components for a consistent UI

            UIManager.put("Label.font", newFont);
            UIManager.put("TextField.font", newFont);
            UIManager.put("Button.font", boldFont);
            UIManager.put("ComboBox.font", newFont);
            UIManager.put("CheckBox.font", newFont);
            UIManager.put("Table.font", new Font("SansSerif", Font.PLAIN, 15));
            UIManager.put("TableHeader.font", boldFont);
            UIManager.put("TitledBorder.font", boldFont);
            UIManager.put("TextArea.font", newFont);
            UIManager.put("TabbedPane.font", boldFont);

            // Adjust UI component sizes for better spacing
            UIManager.put("Table.rowHeight", 26);
            UIManager.put("ComboBox.padding", new Insets(5, 5, 5, 5));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error initializing application: " + e.getMessage());
            System.exit(1);
        }

        // Run the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new HospitalManagementSystem().setVisible(true));
    }
}