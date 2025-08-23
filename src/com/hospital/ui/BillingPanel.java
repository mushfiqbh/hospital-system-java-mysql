package com.hospital.ui;

import com.hospital.db.DatabaseConnector;
import com.hospital.model.PatientItem;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class BillingPanel extends CrudPanel {
    private JTextField billIdField, totalAmountField, paymentDateField;
    private JComboBox<PatientItem> patientComboBox;
    private JComboBox<String> paymentStatusComboBox;

    public BillingPanel() {
        super();
        setupForm();
        loadPatients();
        loadData();
        addButton.addActionListener(_ -> addRecord());
        updateButton.addActionListener(_ -> updateRecord());
        deleteButton.addActionListener(_ -> deleteRecord());
    }

    private void loadPatients() {
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT patient_id, name FROM patients")) {
            patientComboBox.removeAllItems();
            while (rs.next()) {
                patientComboBox.addItem(new PatientItem(rs.getInt("patient_id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + e.getMessage(), "Database Error",
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
        formPanel.add(new JLabel("Bill ID:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Total Amount:"), gbc);

        // Column 2
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        billIdField = new JTextField(15);
        billIdField.setEditable(false);
        billIdField.setText("Auto Generated");
        formPanel.add(billIdField, gbc);
        gbc.gridy++;
        patientComboBox = new JComboBox<>();
        formPanel.add(patientComboBox, gbc);
        gbc.gridy++;
        totalAmountField = new JTextField(15);
        formPanel.add(totalAmountField, gbc);

        // Column 3
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Payment Status:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Payment Date (YYYY-MM-DD):"), gbc);

        // Column 4
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        paymentStatusComboBox = new JComboBox<>();
        paymentStatusComboBox.addItem("Overdue");
        paymentStatusComboBox.addItem("Paid");
        paymentStatusComboBox.addItem("Refunded");
        paymentStatusComboBox.addItem("Cancelled");
        formPanel.add(paymentStatusComboBox, gbc);
        gbc.gridy++;
        paymentDateField = new JTextField(15);
        formPanel.add(paymentDateField, gbc);
    }

    @Override
    protected void loadData() {
        String sql = "SELECT b.bill_id, p.name as patient_name, b.total_amount, b.payment_status, b.payment_date FROM billings b JOIN patients p ON b.patient_id = p.patient_id";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new Vector<>(
                    java.util.Arrays.asList("Bill ID", "Patient", "Total Amount", "Status", "Payment Date")));

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("bill_id"));
                row.add(rs.getString("patient_name"));
                row.add(rs.getBigDecimal("total_amount"));
                row.add(rs.getString("payment_status"));
                row.add(rs.getDate("payment_date"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading billing data: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void addRecord() {
        if (patientComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "INSERT INTO billings (patient_id, total_amount, payment_status, payment_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ((PatientItem) patientComboBox.getSelectedItem()).id);
            pstmt.setBigDecimal(2, new BigDecimal(totalAmountField.getText().trim()));
            pstmt.setString(3, paymentStatusComboBox.getSelectedItem().toString());

            String dateText = paymentDateField.getText().trim();
            if (dateText.isEmpty()) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setDate(4, Date.valueOf(dateText));
            }

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Billing record added successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error adding billing record: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void updateRecord() {
        String sql = "UPDATE billings SET patient_id=?, total_amount=?, payment_status=?, payment_date=? WHERE bill_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ((PatientItem) patientComboBox.getSelectedItem()).id);
            pstmt.setBigDecimal(2, new BigDecimal(totalAmountField.getText().trim()));
            pstmt.setString(3, paymentStatusComboBox.getSelectedItem().toString());

            String dateText = paymentDateField.getText().trim();
            if (dateText.isEmpty()) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setDate(4, Date.valueOf(dateText));
            }
            pstmt.setInt(5, Integer.parseInt(billIdField.getText().trim()));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Billing record updated successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error updating billing record: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void deleteRecord() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this billing record?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        String sql = "DELETE FROM billings WHERE bill_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(billIdField.getText().trim()));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Billing record deleted successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error deleting billing record: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void populateFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row != -1) {
            billIdField.setText(tableModel.getValueAt(row, 0).toString());
            String patientName = tableModel.getValueAt(row, 1).toString();
            totalAmountField.setText(tableModel.getValueAt(row, 2).toString());
            paymentStatusComboBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());

            Object dateValue = tableModel.getValueAt(row, 4);
            paymentDateField.setText(dateValue != null ? dateValue.toString() : "");

            for (int i = 0; i < patientComboBox.getItemCount(); i++) {
                if (patientComboBox.getItemAt(i).name.equals(patientName)) {
                    patientComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    protected void clearForm() {
        billIdField.setText("Auto Generated");
        totalAmountField.setText("");
        paymentStatusComboBox.setSelectedIndex(0);
        paymentDateField.setText("");
        patientComboBox.setSelectedIndex(-1);
        table.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}