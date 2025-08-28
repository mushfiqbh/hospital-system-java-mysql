package com.clinic.model;

public class Doctor {
    private int doctorId;
    private int userId; // New field
    private String name;
    private String specialization;
    private String contact;
    private double consultationFee;
    private String availability;

    public Doctor(int doctorId, int userId, String name, String specialization, String contact, double fee, String availability) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.name = name;
        this.specialization = specialization;
        this.contact = contact;
        this.consultationFee = fee;
        this.availability = availability;
    }
    public int getDoctorId() { return doctorId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getContact() { return contact; }
    public double getConsultationFee() { return consultationFee; }
    public String getAvailability() { return availability; }
}
