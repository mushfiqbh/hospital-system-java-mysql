package com.clinic.dao;

import com.clinic.config.DatabaseHelper;
import com.clinic.model.Bill;
import com.clinic.model.BillingDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDao {
    public boolean addBill(Bill bill) {
        String sql = "INSERT INTO billing(appointment_id, amount, payment_status, payment_method) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bill.getAppointmentId());
            pstmt.setDouble(2, bill.getAmount());
            pstmt.setString(3, bill.getPaymentStatus());
            pstmt.setString(4, bill.getPaymentMethod());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BillingDetails> getAllBillingDetails() {
        List<BillingDetails> bills = new ArrayList<>();
        String sql = "SELECT b.bill_id, b.appointment_id, p.name as patient_name, d.name as doctor_name, b.amount, b.payment_status, b.payment_method " +
                "FROM billing b " +
                "JOIN appointments a ON b.appointment_id = a.appointment_id " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bills.add(new BillingDetails(
                        rs.getInt("bill_id"),
                        rs.getInt("appointment_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_status"),
                        rs.getString("payment_method"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public boolean updateBill(int billId, String status, String method) {
        String sql = "UPDATE billing SET payment_status = ?, payment_method = ? WHERE bill_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, method);
            pstmt.setInt(3, billId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}