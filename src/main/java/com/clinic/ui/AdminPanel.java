package com.clinic.ui;

import com.clinic.dao.DoctorDao;
import com.clinic.dao.UserDao;
import com.clinic.model.Doctor;
import com.clinic.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JPanel {
    private UserDao userDao;
    private DoctorDao doctorDao;
    private JTable userTable;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField userIdField, usernameField, nameField, contactField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    // Doctor specific fields
    private JLabel specLabel, feeLabel, availLabel;
    private JTextField specField, feeField, availField;

    public AdminPanel() {
        userDao = new UserDao();
        doctorDao = new DoctorDao();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"ID", "Username", "Role", "Name", "Contact"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.add(createFormPanel(), BorderLayout.CENTER);
        southPanel.add(createButtonPanel(), BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        refreshUserTable();
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Init components
        userIdField = new JTextField(5);
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        nameField = new JTextField(15);
        contactField = new JTextField(15);
        String[] roles = {"admin", "doctor", "receptionist", "accountant"};
        roleComboBox = new JComboBox<>(roles);

        specField = new JTextField(15);
        feeField = new JTextField(15);
        availField = new JTextField(15);
        specLabel = new JLabel("Specialization:");
        feeLabel = new JLabel("Consultation Fee:");
        availLabel = new JLabel("Availability:");

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1; userIdField.setEditable(false); formPanel.add(userIdField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3; formPanel.add(nameField, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; formPanel.add(usernameField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 3; formPanel.add(contactField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 3; formPanel.add(roleComboBox, gbc);

        // Row 3 (Doctor specific)
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(specLabel, gbc);
        gbc.gridx = 1; formPanel.add(specField, gbc);
        gbc.gridx = 2; formPanel.add(feeLabel, gbc);
        gbc.gridx = 3; formPanel.add(feeField, gbc);

        // Row 4 (Doctor specific)
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(availLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(availField, gbc);

        roleComboBox.addActionListener(e -> toggleDoctorFields());
        toggleDoctorFields(); // Initial state

        return formPanel;
    }

    private void toggleDoctorFields() {
        boolean isDoctor = "doctor".equals(roleComboBox.getSelectedItem());
        specLabel.setVisible(isDoctor);
        specField.setVisible(isDoctor);
        feeLabel.setVisible(isDoctor);
        feeField.setVisible(isDoctor);
        availLabel.setVisible(isDoctor);
        availField.setVisible(isDoctor);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton addButton = new JButton("Add User");
        JButton updateButton = new JButton("Update User");
        JButton deleteButton = new JButton("Delete User");
        JButton clearButton = new JButton("Clear Form");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        clearButton.addActionListener(e -> clearForm());
        return buttonPanel;
    }

    private void refreshUserTable() {
        tableModel.setRowCount(0);
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getUserId(), user.getUsername(), user.getRole(), user.getName(), user.getContact()});
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) return;
        User user = userDao.getUserByUsername(tableModel.getValueAt(selectedRow, 1).toString());
        if (user == null) return;

        userIdField.setText(String.valueOf(user.getUserId()));
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        roleComboBox.setSelectedItem(user.getRole());
        nameField.setText(user.getName());
        contactField.setText(user.getContact());
    }

    private void addUser() {
        User user = new User(0, usernameField.getText(), new String(passwordField.getPassword()), (String) roleComboBox.getSelectedItem(), nameField.getText(), contactField.getText());
        int newUserId = userDao.addUser(user);

        if (newUserId != -1) {
            // If the user is a doctor, also create a doctor record
            if ("doctor".equals(user.getRole())) {
                try {
                    double fee = Double.parseDouble(feeField.getText());
                    Doctor doctor = new Doctor(0, newUserId, user.getName(), specField.getText(), user.getContact(), fee, availField.getText());
                    doctorDao.addDoctor(doctor);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid consultation fee. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    // Consider deleting the created user for consistency
                    userDao.deleteUser(newUserId);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "User added successfully!");
            refreshUserTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user. Username might already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        if (userIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        User user = new User(Integer.parseInt(userIdField.getText()), usernameField.getText(), new String(passwordField.getPassword()), (String) roleComboBox.getSelectedItem(), nameField.getText(), contactField.getText());
        if (userDao.updateUser(user)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            refreshUserTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update user.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        if (userIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int userId = Integer.parseInt(userIdField.getText());
        if (userId == 1) {
            JOptionPane.showMessageDialog(this, "The default admin user cannot be deleted.", "Action Forbidden", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            if (userDao.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                refreshUserTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        userIdField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        nameField.setText("");
        contactField.setText("");
        specField.setText("");
        feeField.setText("");
        availField.setText("");
        userTable.clearSelection();
    }
}