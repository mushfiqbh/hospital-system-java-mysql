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

    // UI Style Constants
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public LoginFrame() {
        userDao = new UserDao();
        setTitle("Clinic Management System - Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User ComboBox
        JLabel userLabel = new JLabel("Select User:");
        userLabel.setFont(LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        userComboBox = new JComboBox<>();
        userComboBox.setFont(FIELD_FONT);
        panel.add(userComboBox, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(FIELD_FONT);
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setFont(LABEL_FONT);
        loginButton.setMargin(new Insets(5, 15, 5, 15));
        panel.add(loginButton, gbc);

        add(panel, BorderLayout.CENTER);
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
        if (username == null || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user and enter a password.", "Error", JOptionPane.ERROR_MESSAGE);
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