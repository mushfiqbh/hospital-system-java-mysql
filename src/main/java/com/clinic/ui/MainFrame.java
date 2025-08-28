package com.clinic.ui;

import com.clinic.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(User user) {
        setTitle("Clinic Management System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel with Welcome Message and Logout Button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose(); // Close the current MainFrame
            new LoginFrame().setVisible(true); // Open a new LoginFrame
        });

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel with Role-Specific Dashboards
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        mainPanel.add(new AdminPanel(), "admin");
        mainPanel.add(new ReceptionistPanel(), "receptionist");
        mainPanel.add(new DoctorPanel(user), "doctor");
        mainPanel.add(new AccountantPanel(), "accountant");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, user.getRole());
    }
}