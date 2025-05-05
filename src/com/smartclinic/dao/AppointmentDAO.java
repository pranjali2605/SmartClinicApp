package com.smartclinic.dao;

import com.smartclinic.model.Appointment;

import java.sql.*;
import java.util.*;

public class AppointmentDAO {
    private final Map<String, Queue<String>> waitlists = new HashMap<>();

    public boolean bookAppointment(Appointment appt) {
        if (isSlotAvailable(appt.getDoctorId(), appt.getDate(), appt.getTimeSlot())) {
            return saveAppointment(appt);
        } else {
            enqueueWaitlist(appt.getDoctorId(), appt.getDate(), appt.getTimeSlot(), appt.getPatientId());
            System.out.println("[!] Slot is full. Patient added to waitlist.");
            return false;
        }
    }

    private boolean saveAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments (id, patient_id, doctor_id, date, time_slot, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appt.getId());
            stmt.setString(2, appt.getPatientId());
            stmt.setString(3, appt.getDoctorId());
            stmt.setString(4, appt.getDate());
            stmt.setString(5, appt.getTimeSlot());
            stmt.setString(6, appt.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error booking appointment: " + e.getMessage());
            return false;
        }
    }

    public boolean isSlotAvailable(String doctorId, String date, String timeSlot) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND date = ? AND time_slot = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctorId);
            stmt.setString(2, date);
            stmt.setString(3, timeSlot);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) < 1;

        } catch (SQLException e) {
            System.out.println("Error checking availability: " + e.getMessage());
            return false;
        }
    }

    private void enqueueWaitlist(String doctorId, String date, String timeSlot, String patientId) {
        String key = buildWaitlistKey(doctorId, date, timeSlot);
        waitlists.computeIfAbsent(key, k -> new LinkedList<>()).add(patientId);
    }

    private String buildWaitlistKey(String doctorId, String date, String timeSlot) {
        return doctorId + "_" + date + "_" + timeSlot;
    }

    public boolean cancelAppointment(String appointmentId) {
        String selectSql = "SELECT * FROM appointments WHERE id = ?";
        String deleteSql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            // Step 1: Fetch the appointment details before deleting
            selectStmt.setString(1, appointmentId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Appointment not found.");
                return false;
            }

            String doctorId = rs.getString("doctor_id");
            String date = rs.getString("date");
            String timeSlot = rs.getString("time_slot");

            // Step 2: Delete the appointment
            deleteStmt.setString(1, appointmentId);
            int rowsDeleted = deleteStmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("[✓] Appointment cancelled.");
                reassignFromWaitlist(doctorId, date, timeSlot);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error canceling appointment: " + e.getMessage());
        }

        return false;
    }

    private void reassignFromWaitlist(String doctorId, String date, String timeSlot) {
        String key = buildWaitlistKey(doctorId, date, timeSlot);
        Queue<String> queue = waitlists.get(key);

        if (queue != null && !queue.isEmpty()) {
            String nextPatientId = queue.poll();
            String newApptId = "A" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            Appointment reassigned = new Appointment(
                    newApptId,
                    nextPatientId,
                    doctorId,
                    date,
                    timeSlot,
                    "Confirmed"
            );

            if (saveAppointment(reassigned)) {
                System.out.println("[✓] Reassigned slot to patient in waitlist: " + nextPatientId);
            } else {
                System.out.println("[✗] Failed to reassign slot.");
            }
        } else {
            System.out.println("[ℹ] No waitlisted patients for this slot.");
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY date, time_slot";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Appointment(
                        rs.getString("id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getString("date"),
                        rs.getString("time_slot"),
                        rs.getString("status")));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching appointments: " + e.getMessage());
        }

        return list;
    }

    public Appointment getAppointmentById(String id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Appointment(
                        rs.getString("id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getString("date"),
                        rs.getString("time_slot"),
                        rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching appointment: " + e.getMessage());
        }
        return null;
    }
}
