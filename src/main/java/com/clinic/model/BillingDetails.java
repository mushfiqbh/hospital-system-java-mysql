package com.clinic.model;

/**
 * A special model class that extends Bill to include joined data for UI display.
 */
public class BillingDetails extends Bill {
    private String patientName;
    private String doctorName;

    public BillingDetails(int billId, int appointmentId, double amount, String paymentStatus, String paymentMethod, String patientName, String doctorName) {
        // Call the constructor of the parent class (Bill)
        super(billId, appointmentId, amount, paymentStatus, paymentMethod);
        this.patientName = patientName;
        this.doctorName = doctorName;
    }

    // Getters for the additional fields
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
}