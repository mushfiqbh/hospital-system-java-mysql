package com.clinic.ui;

import com.clinic.dao.AppointmentDao;
import com.clinic.dao.DoctorDao;
import com.clinic.model.Appointment;
import com.clinic.model.Doctor;
import com.clinic.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {
    private AppointmentDao appointmentDao;
    private DoctorDao doctorDao;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private User loggedInUser;
    private Doctor doctorProfile;
    private List<Appointment> appointmentList;
    private JTextField apptIdField = new JTextField(5);
    private JComboBox<String> statusComboBox;
    private JTextArea notesArea = new JTextArea(3, 20);
    private JTextField prescriptionField = new JTextField(10);

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public DoctorPanel(User user) {
        this.loggedInUser = user;
        this.appointmentDao = new AppointmentDao();
        this.doctorDao = new DoctorDao();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Doctor Dashboard - Welcome Dr. " + user.getName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Appt ID", "Patient ID", "Date", "Time", "Status", "Notes", "Prescription Code"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(tableModel);
        appointmentTable.setFont(FIELD_FONT);
        appointmentTable.setRowHeight(25);
        appointmentTable.getTableHeader().setFont(LABEL_FONT);
        add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        add(createFormPanel(), BorderLayout.SOUTH);

        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && appointmentTable.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        if (loggedInUser != null && "doctor".equals(loggedInUser.getRole())) {
            loadDoctorProfileAndAppointments();
        }
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Appointment"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        apptIdField.setEditable(false);
        String[] statuses = {"scheduled", "completed", "canceled"};
        statusComboBox = new JComboBox<>(statuses);

        // Apply fonts
        apptIdField.setFont(FIELD_FONT);
        statusComboBox.setFont(FIELD_FONT);
        notesArea.setFont(FIELD_FONT);
        prescriptionField.setFont(FIELD_FONT);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Appointment ID:"){{setFont(LABEL_FONT);}}, gbc);
        gbc.gridx = 1; formPanel.add(apptIdField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Status:"){{setFont(LABEL_FONT);}}, gbc);
        gbc.gridx = 3; formPanel.add(statusComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Prescription Code:"){{setFont(LABEL_FONT);}}, gbc);
        gbc.gridx = 1; formPanel.add(prescriptionField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Notes:"){{setFont(LABEL_FONT);}}, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JScrollPane(notesArea), gbc);

        JButton updateButton = new JButton("Update Appointment");
        updateButton.setFont(LABEL_FONT);
        updateButton.addActionListener(e -> updateAppointment());

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(updateButton, BorderLayout.EAST);

        return panel;
    }

    private void loadDoctorProfileAndAppointments() {
        this.doctorProfile = doctorDao.getDoctorByUserId(loggedInUser.getUserId());
        if (this.doctorProfile != null) {
            refreshAppointmentTable();
        } else {
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Could not find a doctor profile for your user account.", "Profile Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAppointmentTable() {
        tableModel.setRowCount(0);
        if (doctorProfile == null) return;

        appointmentList = appointmentDao.getAppointmentsByDoctorId(doctorProfile.getDoctorId());
        for (Appointment a : appointmentList) {
            tableModel.addRow(new Object[]{a.getAppointmentId(), a.getPatientId(), a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus(), a.getNotes(), a.getPrescriptionCode() == 0 ? "" : a.getPrescriptionCode()});
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) return;

        Appointment selectedAppt = appointmentList.get(selectedRow);
        apptIdField.setText(String.valueOf(selectedAppt.getAppointmentId()));
        statusComboBox.setSelectedItem(selectedAppt.getStatus());
        notesArea.setText(selectedAppt.getNotes());
        int pCode = selectedAppt.getPrescriptionCode();
        prescriptionField.setText(pCode == 0 ? "" : String.valueOf(pCode));
    }

    private void updateAppointment() {
        if (apptIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = appointmentTable.getSelectedRow();
        Appointment apptToUpdate = appointmentList.get(selectedRow);

        String status = (String) statusComboBox.getSelectedItem();
        String notes = notesArea.getText();
        int prescriptionCode = 0;
        try {
            if (!prescriptionField.getText().trim().isEmpty()) {
                prescriptionCode = Integer.parseInt(prescriptionField.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prescription code must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Appointment updatedAppointment = new Appointment(
                apptToUpdate.getAppointmentId(),
                apptToUpdate.getPatientId(),
                apptToUpdate.getDoctorId(),
                apptToUpdate.getAppointmentDate(),
                apptToUpdate.getAppointmentTime(),
                status,
                notes,
                prescriptionCode
        );

        if (appointmentDao.updateAppointment(updatedAppointment)) {
            JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
            refreshAppointmentTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update appointment.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        apptIdField.setText("");
        statusComboBox.setSelectedIndex(0);
        notesArea.setText("");
        prescriptionField.setText("");
        appointmentTable.clearSelection();
    }
}