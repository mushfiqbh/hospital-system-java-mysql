package com.clinic.ui;

import com.clinic.dao.UserDao;
import com.clinic.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {
    private JComboBox<String> userComboBox;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserDao userDao;

    public LoginFrame() {
        userDao = new UserDao();
        setTitle("Clinic Management System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User ComboBox
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1;
        userComboBox = new JComboBox<>();
        panel.add(userComboBox, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Populate the dropdown with users
        populateUserComboBox();

        loginButton.addActionListener(e -> performLogin());
    }

    private void populateUserComboBox() {
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            userComboBox.addItem(user.getUsername());
        }
    }

    private void performLogin() {
        String username = (String) userComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword());

        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDao.validateUser(username, password);

        if (user != null) {
            dispose();
            new MainFrame(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid password for the selected user.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}