package com.hospital.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class CrudPanel extends JPanel {
    protected DefaultTableModel tableModel;
    protected JTable table;
    protected JPanel formPanel;
    protected JButton addButton, updateButton, deleteButton, clearButton;

    public CrudPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Container for Form and Buttons ---
        JPanel topPanel = new JPanel(new BorderLayout());

        // Form Panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Record"));

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear Form");

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Add form and buttons to the top container
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table Setup
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
    table = new JTable(tableModel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(table.getRowHeight() + 10);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Records"));

        // Add top container and table to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


        // --- Event Listeners ---

        // When a table row is selected, enable update/delete and populate the form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
                populateFormFromSelectedRow();
            }
        });

        // Clear button action
        clearButton.addActionListener(_ -> clearForm());
    }

    // Abstract methods to be implemented by subclasses
    protected abstract void setupForm();
    protected abstract void loadData();
    protected abstract void addRecord();
    protected abstract void updateRecord();
    protected abstract void deleteRecord();
    protected abstract void populateFormFromSelectedRow();
    protected abstract void clearForm();
}