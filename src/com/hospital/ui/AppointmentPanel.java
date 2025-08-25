package com.hospital.ui;

import com.hospital.db.DatabaseConnector;
import com.hospital.model.DoctorItem;
import com.hospital.model.PatientItem;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AppointmentPanel extends CrudPanel {
    private JTextField appointmentIdField, appDateField, appTimeField;
    private JComboBox<PatientItem> patientComboBox;
    private JComboBox<DoctorItem> doctorComboBox;
    private JComboBox<String> statusComboBox;

    public AppointmentPanel() {
        super();
        setupForm();
        loadForeignKeys();
        loadData();
        addButton.addActionListener(_ -> addRecord());
        updateButton.addActionListener(_ -> updateRecord());
        deleteButton.addActionListener(_ -> deleteRecord());
    }

    private void loadForeignKeys() {
        try (Connection conn = DatabaseConnector.getConnection(); Statement stmt = conn.createStatement()) {
            // Load patients
            patientComboBox.removeAllItems();
            ResultSet rsPatients = stmt.executeQuery("SELECT patient_id, name FROM patients");
            while (rsPatients.next()) {
                patientComboBox.addItem(new PatientItem(rsPatients.getInt("patient_id"), rsPatients.getString("name")));
            }

            // Load doctors
            doctorComboBox.removeAllItems();
            ResultSet rsDoctors = stmt.executeQuery("SELECT doctor_id, name FROM doctors");
            while (rsDoctors.next()) {
                doctorComboBox.addItem(new DoctorItem(rsDoctors.getInt("doctor_id"), rsDoctors.getString("name")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patients/doctors: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void setupForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Column 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Appt. ID:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Doctor:"), gbc);

        // Column 2
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        appointmentIdField = new JTextField(15);
        appointmentIdField.setEditable(false);
        appointmentIdField.setText("Auto Generated");
        formPanel.add(appointmentIdField, gbc);
        gbc.gridy++;
        patientComboBox = new JComboBox<>();
        formPanel.add(patientComboBox, gbc);
        gbc.gridy++;
        doctorComboBox = new JComboBox<>();
        formPanel.add(doctorComboBox, gbc);

        // Column 3
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Time (HH:MM:SS):"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Status:"), gbc);

        // Column 4
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        appDateField = new JTextField(15);
        formPanel.add(appDateField, gbc);
        gbc.gridy++;
        appTimeField = new JTextField(15);
        formPanel.add(appTimeField, gbc);
        gbc.gridy++;
        statusComboBox = new JComboBox<>();
        statusComboBox.addItem("Scheduled");
        statusComboBox.addItem("Completed");
        statusComboBox.addItem("Cancelled");
        formPanel.add(statusComboBox, gbc);
    }

    @Override
    protected void loadData() {
        String sql = "SELECT a.appointment_id, p.name as patient_name, d.name as doctor_name, a.appointment_date, a.appointment_time, a.status FROM appointments a JOIN patients p ON a.patient_id = p.patient_id JOIN doctors d ON a.doctor_id = d.doctor_id";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(
                    new Vector<>(java.util.Arrays.asList("Appt ID", "Patient", "Doctor", "Date", "Time", "Status")));

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("appointment_id"));
                row.add(rs.getString("patient_name"));
                row.add(rs.getString("doctor_name"));
                row.add(rs.getString("appointment_date")); // Read date as TEXT
                row.add(rs.getString("appointment_time")); // Read time as TEXT
                row.add(rs.getString("status"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void addRecord() {
        if (patientComboBox.getSelectedItem() == null || doctorComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient and a doctor.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ((PatientItem) patientComboBox.getSelectedItem()).id);
            pstmt.setInt(2, ((DoctorItem) doctorComboBox.getSelectedItem()).id);
            pstmt.setString(3, appDateField.getText().trim()); // Store date as TEXT
            pstmt.setString(4, appTimeField.getText().trim()); // Store time as TEXT
            pstmt.setString(5, statusComboBox.getSelectedItem().toString());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment added successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error adding appointment: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void updateRecord() {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, appointment_time=?, status=? WHERE appointment_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ((PatientItem) patientComboBox.getSelectedItem()).id);
            pstmt.setInt(2, ((DoctorItem) doctorComboBox.getSelectedItem()).id);
            pstmt.setString(3, appDateField.getText().trim());
            pstmt.setString(4, appTimeField.getText().trim());
            pstmt.setString(5, statusComboBox.getSelectedItem().toString());
            pstmt.setInt(6, Integer.parseInt(appointmentIdField.getText().trim()));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment updated successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error updating appointment: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void deleteRecord() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(appointmentIdField.getText().trim()));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment deleted successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error deleting appointment: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void populateFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row != -1) {
            appointmentIdField.setText(tableModel.getValueAt(row, 0).toString());
            String patientName = tableModel.getValueAt(row, 1).toString();
            String doctorName = tableModel.getValueAt(row, 2).toString();
            appDateField.setText(tableModel.getValueAt(row, 3).toString());
            appTimeField.setText(tableModel.getValueAt(row, 4).toString());
            statusComboBox.setSelectedItem(tableModel.getValueAt(row, 5).toString());

            // Select the correct patient in the combo box
            for (int i = 0; i < patientComboBox.getItemCount(); i++) {
                if (patientComboBox.getItemAt(i).name.equals(patientName)) {
                    patientComboBox.setSelectedIndex(i);
                    break;
                }
            }
            // Select the correct doctor in the combo box
            for (int i = 0; i < doctorComboBox.getItemCount(); i++) {
                if (doctorComboBox.getItemAt(i).name.equals(doctorName)) {
                    doctorComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    protected void clearForm() {
        appointmentIdField.setText("Auto Generated");
        appDateField.setText("");
        appTimeField.setText("");
        statusComboBox.setSelectedIndex(-1);
        patientComboBox.setSelectedIndex(-1);
        doctorComboBox.setSelectedIndex(-1);
        table.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
