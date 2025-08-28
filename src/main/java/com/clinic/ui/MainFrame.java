package com.clinic.ui;

import com.clinic.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(User user) {
        setTitle("Clinic Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

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