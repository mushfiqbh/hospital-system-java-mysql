package com.clinic.model;

public class Bill {
    private int billId;
    private int appointmentId;
    private double amount;
    private String paymentStatus;
    private String paymentMethod;

    public Bill(int billId, int appointmentId, double amount, String paymentStatus, String paymentMethod) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
    }
    public int getBillId() { return billId; }
    public int getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
}