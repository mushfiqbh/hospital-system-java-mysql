package com.clinic.model;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private String notes;
    private int prescriptionCode;

    public Appointment(int appointmentId, int patientId, int doctorId, String appointmentDate, String appointmentTime, String status, String notes, int prescriptionCode) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.notes = notes;
        this.prescriptionCode = prescriptionCode;
    }
    public int getAppointmentId() { return appointmentId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public int getPrescriptionCode() { return prescriptionCode; }
}