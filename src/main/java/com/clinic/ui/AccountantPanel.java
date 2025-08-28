package com.clinic.ui;

import com.clinic.dao.BillingDao;
import com.clinic.model.BillingDetails;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccountantPanel extends JPanel {
    private BillingDao billingDao;
    private JTable billingTable;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField billIdField = new JTextField(5);
    private JComboBox<String> statusComboBox;
    private JTextField methodField = new JTextField(15);

    public AccountantPanel() {
        billingDao = new BillingDao();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Center: Billing Table
        String[] columnNames = {"Bill ID", "Patient Name", "Doctor Name", "Amount", "Payment Status", "Payment Method"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        billingTable = new JTable(tableModel);
        add(new JScrollPane(billingTable), BorderLayout.CENTER);

        // South: Form and Buttons
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.add(createFormPanel(), BorderLayout.CENTER);
        southPanel.add(createButtonPanel(), BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        billingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && billingTable.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        refreshBillingTable();
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createTitledBorder("Update Bill"));

        billIdField.setEditable(false);
        formPanel.add(new JLabel("Bill ID:"));
        formPanel.add(billIdField);

        String[] statuses = {"unpaid", "paid", "partial"};
        statusComboBox = new JComboBox<>(statuses);
        formPanel.add(new JLabel("Payment Status:"));
        formPanel.add(statusComboBox);

        formPanel.add(new JLabel("Payment Method:"));
        formPanel.add(methodField);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton updateButton = new JButton("Update Bill");
        JButton clearButton = new JButton("Clear Selection");

        updateButton.addActionListener(e -> updateBill());
        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        return buttonPanel;
    }

    private void refreshBillingTable() {
        tableModel.setRowCount(0);
        List<BillingDetails> bills = billingDao.getAllBillingDetails();
        for (BillingDetails bill : bills) {
            tableModel.addRow(new Object[]{
                    bill.getBillId(),
                    bill.getPatientName(),
                    bill.getDoctorName(),
                    bill.getAmount(),
                    bill.getPaymentStatus(),
                    bill.getPaymentMethod()
            });
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = billingTable.getSelectedRow();
        if (selectedRow < 0) return;

        billIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
        Object method = tableModel.getValueAt(selectedRow, 5);
        methodField.setText(method != null ? method.toString() : "");
    }

    private void updateBill() {
        if (billIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a bill from the table to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int billId = Integer.parseInt(billIdField.getText());
        String status = (String) statusComboBox.getSelectedItem();
        String method = methodField.getText();

        if (billingDao.updateBill(billId, status, method)) {
            JOptionPane.showMessageDialog(this, "Bill updated successfully!");
            refreshBillingTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update bill.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        billIdField.setText("");
        statusComboBox.setSelectedIndex(0);
        methodField.setText("");
        billingTable.clearSelection();
    }
}