package com.clinic.model;

public class Patient {
    private int patientId;
    private String name;
    private String gender;
    private String dateOfBirth;
    private String contact;
    private String address;

    public Patient(int patientId, String name, String gender, String dateOfBirth, String contact, String address) {
        this.patientId = patientId;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.address = address;
    }
    public int getPatientId() { return patientId; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }
}