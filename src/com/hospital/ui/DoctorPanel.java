package com.hospital.ui;

import com.hospital.db.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DoctorPanel extends CrudPanel {
    private JTextField doctorIdField, nameField, specializationField, phoneField, emailField, departmentField;

    public DoctorPanel() {
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

        // Column 1: Labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Doctor ID:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Specialization:"), gbc);

        // Column 2: TextFields
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        doctorIdField = new JTextField(15);
        doctorIdField.setEditable(false);
        doctorIdField.setText("Auto Generated");
        formPanel.add(doctorIdField, gbc);
        gbc.gridy++;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);
        gbc.gridy++;
        specializationField = new JTextField(15);
        formPanel.add(specializationField, gbc);

        // Column 3: Labels
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Department:"), gbc);

        // Column 4: TextFields
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);
        gbc.gridy++;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);
        gbc.gridy++;
        departmentField = new JTextField(15);
        formPanel.add(departmentField, gbc);
    }

    @Override
    protected void loadData() {
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT doctor_id, name, specialization, phone, email, department FROM doctors")) {

            tableModel.setRowCount(0); // Clear existing data
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Set column headers
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            // Add rows from result set
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading doctor data: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void addRecord() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Doctor Name is required.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // SQL INSERT statement without the doctor_id column
        String sql = "INSERT INTO doctors (name, specialization, phone, email, department) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters starting from index 1
            pstmt.setString(1, nameField.getText().trim());
            pstmt.setString(2, specializationField.getText().trim());
            pstmt.setString(3, phoneField.getText().trim());
            pstmt.setString(4, emailField.getText().trim());
            pstmt.setString(5, departmentField.getText().trim());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Doctor added successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData(); // Refresh table
                clearForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding doctor: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void updateRecord() {
        String sql = "UPDATE doctors SET name = ?, specialization = ?, phone = ?, email = ?, department = ? WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText().trim());
            pstmt.setString(2, specializationField.getText().trim());
            pstmt.setString(3, phoneField.getText().trim());
            pstmt.setString(4, emailField.getText().trim());
            pstmt.setString(5, departmentField.getText().trim());
            pstmt.setInt(6, Integer.parseInt(doctorIdField.getText().trim())); // Get ID from the non-editable field

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Doctor updated successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Doctor not found or no changes made.", "Update Failed",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating doctor: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void deleteRecord() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this doctor?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(doctorIdField.getText().trim()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error deleting doctor: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void populateFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            doctorIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            specializationField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            departmentField.setText(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    @Override
    protected void clearForm() {
        doctorIdField.setText("Auto Generated");
        nameField.setText("");
        specializationField.setText("");
        phoneField.setText("");
        emailField.setText("");
        departmentField.setText("");
        table.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}