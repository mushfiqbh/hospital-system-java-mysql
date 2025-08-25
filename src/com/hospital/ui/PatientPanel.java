package com.hospital.ui;

import com.hospital.db.DatabaseConnector;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class PatientPanel extends CrudPanel {
    private JTextField patientIdField, nameField, genderField, ageField, phoneField, addressField, admissionDateField;

    public PatientPanel() {
        super();
        setupForm();
        loadData();
        addButton.addActionListener(_ -> addRecord());
        updateButton.addActionListener(_ -> updateRecord());
        deleteButton.addActionListener(_ -> deleteRecord());
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
        formPanel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Age:"), gbc);

        // Column 2
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        patientIdField = new JTextField(15);
        patientIdField.setEditable(false);
        formPanel.add(patientIdField, gbc);
        gbc.gridy++;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);
        gbc.gridy++;
        genderField = new JTextField(15);
        formPanel.add(genderField, gbc);
        gbc.gridy++;
        ageField = new JTextField(15);
        formPanel.add(ageField, gbc);

        // Column 3
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Admission Date (YYYY-MM-DD):"), gbc);

        // Column 4
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);
        gbc.gridy++;
        addressField = new JTextField(15);
        formPanel.add(addressField, gbc);
        gbc.gridy++;
        admissionDateField = new JTextField(15);
        formPanel.add(admissionDateField, gbc);
    }

    @Override
    protected void loadData() {
        String sql = "SELECT patient_id, name, gender, age, phone, address, admission_date FROM patients";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            tableModel.setRowCount(0);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient data: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void addRecord() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient Name is required.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "INSERT INTO patients (name, gender, age, phone, address, admission_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText().trim());
            pstmt.setString(2, genderField.getText().trim());
            pstmt.setInt(3, Integer.parseInt(ageField.getText().trim()));
            pstmt.setString(4, phoneField.getText().trim());
            pstmt.setString(5, addressField.getText().trim());
            pstmt.setString(6, admissionDateField.getText().trim()); // Store date as TEXT

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient added successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error adding patient: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void updateRecord() {
        String sql = "UPDATE patients SET name=?, gender=?, age=?, phone=?, address=?, admission_date=? WHERE patient_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText().trim());
            pstmt.setString(2, genderField.getText().trim());
            pstmt.setInt(3, Integer.parseInt(ageField.getText().trim()));
            pstmt.setString(4, phoneField.getText().trim());
            pstmt.setString(5, addressField.getText().trim());
            pstmt.setString(6, admissionDateField.getText().trim()); // Store date as TEXT
            pstmt.setInt(7, Integer.parseInt(patientIdField.getText().trim()));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient updated successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error updating patient: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void deleteRecord() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(patientIdField.getText().trim()));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void populateFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row != -1) {
            patientIdField.setText(tableModel.getValueAt(row, 0).toString());
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            genderField.setText(tableModel.getValueAt(row, 2).toString());
            ageField.setText(tableModel.getValueAt(row, 3).toString());
            phoneField.setText(tableModel.getValueAt(row, 4).toString());
            addressField.setText(tableModel.getValueAt(row, 5).toString());
            admissionDateField.setText(tableModel.getValueAt(row, 6).toString());
        }
    }

    @Override
    protected void clearForm() {
        patientIdField.setText("");
        nameField.setText("");
        genderField.setText("");
        ageField.setText("");
        phoneField.setText("");
        addressField.setText("");
        admissionDateField.setText("");
        table.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}