package com.clinic.ui;

import com.clinic.dao.AppointmentDao;
import com.clinic.dao.BillingDao;
import com.clinic.dao.DoctorDao;
import com.clinic.dao.PatientDao;
import com.clinic.model.Appointment;
import com.clinic.model.Bill;
import com.clinic.model.Doctor;
import com.clinic.model.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceptionistPanel extends JPanel {
    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private AppointmentDao appointmentDao;
    private BillingDao billingDao;
    private List<Patient> patientList;
    private List<Doctor> doctorList;
    private JTable patientTable;
    private DefaultTableModel patientTableModel;
    private JTextField patientIdField, patientNameField, patientDobField, patientContactField, patientAddressField;
    private JComboBox<String> patientGenderCombo;
    private JComboBox<String> apptPatientComboBox;
    private JComboBox<String> apptDoctorComboBox;
    private JTextField apptDateField, apptTimeField;

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public ReceptionistPanel() {
        patientDao = new PatientDao();
        doctorDao = new DoctorDao();
        appointmentDao = new AppointmentDao();
        billingDao = new BillingDao();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LABEL_FONT);
        tabbedPane.addTab("Patient Management", createPatientPanel());
        tabbedPane.addTab("Appointment Scheduling", createAppointmentPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        String[] columnNames = {"ID", "Name", "Gender", "DOB", "Contact", "Address"};
        patientTableModel = new DefaultTableModel(columnNames, 0);
        patientTable = new JTable(patientTableModel);
        patientTable.setFont(FIELD_FONT);
        patientTable.setRowHeight(25);
        patientTable.getTableHeader().setFont(LABEL_FONT);
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(15, 15));
        southPanel.add(createPatientFormPanel(), BorderLayout.CENTER);
        southPanel.add(createPatientButtonPanel(), BorderLayout.EAST);
        panel.add(southPanel, BorderLayout.SOUTH);

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patientTable.getSelectedRow() != -1) {
                populatePatientFormFromSelectedRow();
            }
        });

        refreshPatientTable();
        return panel;
    }

    private JPanel createPatientFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));

        patientIdField = new JTextField();
        patientIdField.setEditable(false);
        patientNameField = new JTextField();
        patientGenderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        patientDobField = new JTextField();
        patientContactField = new JTextField();
        patientAddressField = new JTextField();

        // Apply fonts
        patientIdField.setFont(FIELD_FONT);
        patientNameField.setFont(FIELD_FONT);
        patientGenderCombo.setFont(FIELD_FONT);
        patientDobField.setFont(FIELD_FONT);
        patientContactField.setFont(FIELD_FONT);
        patientAddressField.setFont(FIELD_FONT);

        formPanel.add(new JLabel("Patient ID:"){{setFont(LABEL_FONT);}});
        formPanel.add(patientIdField);
        formPanel.add(new JLabel("Name:"){{setFont(LABEL_FONT);}});
        formPanel.add(patientNameField);
        formPanel.add(new JLabel("Gender:"){{setFont(LABEL_FONT);}});
        formPanel.add(patientGenderCombo);
        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"){{setFont(LABEL_FONT);}});
        formPanel.add(patientDobField);
        formPanel.add(new JLabel("Contact:"){{setFont(LABEL_FONT);}});
        formPanel.add(patientContactField);
        formPanel.add(new JLabel("Address:"){{setFont(LABEL_FONT);}});
        formPanel.add(patientAddressField);

        return formPanel;
    }

    private JPanel createPatientButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton addButton = new JButton("Add Patient");
        JButton updateButton = new JButton("Update Patient");
        JButton deleteButton = new JButton("Delete Patient");
        JButton clearButton = new JButton("Clear Form");

        addButton.setFont(LABEL_FONT);
        updateButton.setFont(LABEL_FONT);
        deleteButton.setFont(LABEL_FONT);
        clearButton.setFont(LABEL_FONT);

        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());
        clearButton.addActionListener(e -> clearPatientForm());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        return buttonPanel;
    }

    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Appointment"));

        apptPatientComboBox = new JComboBox<>();
        apptDoctorComboBox = new JComboBox<>();
        apptDateField = new JTextField(LocalDate.now().toString());
        apptTimeField = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        apptPatientComboBox.setFont(FIELD_FONT);
        apptDoctorComboBox.setFont(FIELD_FONT);
        apptDateField.setFont(FIELD_FONT);
        apptTimeField.setFont(FIELD_FONT);

        formPanel.add(new JLabel("Select Patient:"){{setFont(LABEL_FONT);}});
        formPanel.add(apptPatientComboBox);
        formPanel.add(new JLabel("Select Doctor:"){{setFont(LABEL_FONT);}});
        formPanel.add(apptDoctorComboBox);
        formPanel.add(new JLabel("Appointment Date (YYYY-MM-DD):"){{setFont(LABEL_FONT);}});
        formPanel.add(apptDateField);
        formPanel.add(new JLabel("Appointment Time (HH:MM):"){{setFont(LABEL_FONT);}});
        formPanel.add(apptTimeField);

        JButton createAppointmentButton = new JButton("Create Appointment & Generate Bill");
        createAppointmentButton.setFont(LABEL_FONT);
        createAppointmentButton.addActionListener(e -> createAppointment());

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(createAppointmentButton, BorderLayout.SOUTH);

        refreshAppointmentFormComboBoxes();
        return panel;
    }

    private void addPatient() {
        Patient newPatient = new Patient(0, patientNameField.getText(), (String)patientGenderCombo.getSelectedItem(), patientDobField.getText(), patientContactField.getText(), patientAddressField.getText());
        if (patientDao.addPatient(newPatient)) {
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            refreshPatientTable();
            refreshAppointmentFormComboBoxes();
            clearPatientForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add patient.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatient() {
        if (patientIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Patient patient = new Patient(
                Integer.parseInt(patientIdField.getText()),
                patientNameField.getText(),
                (String) patientGenderCombo.getSelectedItem(),
                patientDobField.getText(),
                patientContactField.getText(),
                patientAddressField.getText()
        );
        if (patientDao.updatePatient(patient)) {
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            refreshPatientTable();
            refreshAppointmentFormComboBoxes();
            clearPatientForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update patient.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        if (patientIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int patientId = Integer.parseInt(patientIdField.getText());
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            if (patientDao.deletePatient(patientId)) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                refreshPatientTable();
                refreshAppointmentFormComboBoxes();
                clearPatientForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete patient. They may have existing appointments.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearPatientForm() {
        patientIdField.setText("");
        patientNameField.setText("");
        patientGenderCombo.setSelectedIndex(0);
        patientDobField.setText("");
        patientContactField.setText("");
        patientAddressField.setText("");
        patientTable.clearSelection();
    }

    private void populatePatientFormFromSelectedRow() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) return;
        Patient selectedPatient = patientList.get(selectedRow);
        patientIdField.setText(String.valueOf(selectedPatient.getPatientId()));
        patientNameField.setText(selectedPatient.getName());
        patientGenderCombo.setSelectedItem(selectedPatient.getGender());
        patientDobField.setText(selectedPatient.getDateOfBirth());
        patientContactField.setText(selectedPatient.getContact());
        patientAddressField.setText(selectedPatient.getAddress());
    }

    private void createAppointment() {
        int patientIndex = apptPatientComboBox.getSelectedIndex();
        int doctorIndex = apptDoctorComboBox.getSelectedIndex();
        if (patientIndex < 0 || doctorIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient and a doctor.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Patient selectedPatient = patientList.get(patientIndex);
        Doctor selectedDoctor = doctorList.get(doctorIndex);
        Appointment newAppointment = new Appointment(0, selectedPatient.getPatientId(), selectedDoctor.getDoctorId(), apptDateField.getText(), apptTimeField.getText() + ":00", "scheduled", "", 0);
        int newAppointmentId = appointmentDao.addAppointment(newAppointment);
        if (newAppointmentId != -1) {
            Bill newBill = new Bill(0, newAppointmentId, selectedDoctor.getConsultationFee(), "unpaid", null);
            if (billingDao.addBill(newBill)) {
                JOptionPane.showMessageDialog(this, "Appointment created and bill generated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Appointment created, but failed to generate bill.", "Billing Error", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create appointment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshPatientTable() {
        patientList = patientDao.getAllPatients();
        patientTableModel.setRowCount(0);
        for (Patient p : patientList) {
            patientTableModel.addRow(new Object[]{p.getPatientId(), p.getName(), p.getGender(), p.getDateOfBirth(), p.getContact(), p.getAddress()});
        }
    }

    private void refreshAppointmentFormComboBoxes() {
        patientList = patientDao.getAllPatients();
        apptPatientComboBox.removeAllItems();
        for (Patient p : patientList) {
            apptPatientComboBox.addItem(p.getPatientId() + ": " + p.getName());
        }
        doctorList = doctorDao.getAllDoctors();
        apptDoctorComboBox.removeAllItems();
        for (Doctor d : doctorList) {
            apptDoctorComboBox.addItem(d.getName() + " (" + d.getSpecialization() + ")");
        }
    }
}