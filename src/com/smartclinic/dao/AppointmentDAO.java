package com.smartclinic.dao;

import com.smartclinic.model.Appointment;
import com.smartclinic.util.SpecializationMapper;

import java.sql.*;
import java.util.*;

public class AppointmentDAO {
    private final Map<String, Queue<String>> waitlists = new HashMap<>();

    // Book appointment using issue instead of doctorId directly
    public boolean bookAppointment(String issue, Appointment appt) {
        String specialization = SpecializationMapper.getSpecialization(issue);
        String doctorId = getDoctorIdBySpecialization(specialization);

        if (doctorId == null) {
            System.out.println("[✗] No doctor found for specialization: " + specialization);
            return false;
        }

        appt.setDoctorId(doctorId); // set doctorId in appointment

        if (isSlotAvailable(doctorId, appt.getDate(), appt.getTimeSlot())) {
            return saveAppointment(appt);
        } else {
            enqueueWaitlist(doctorId, appt.getDate(), appt.getTimeSlot(), appt.getPatientId());
            System.out.println("[!] Slot is full. Patient added to waitlist.");
            return false;
        }
    }

    // Save appointment to DB
    private boolean saveAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments (id, patient_id, doctor_id, date, time_slot, issue, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appt.getId());
            stmt.setString(2, appt.getPatientId());
            stmt.setString(3, appt.getDoctorId());
            stmt.setString(4, appt.getDate());
            stmt.setString(5, appt.getTimeSlot());
            stmt.setString(6,appt.getIssue());
            stmt.setString(7, appt.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[✗] Error booking appointment: " + e.getMessage());
            return false;
        }
    }

    // Fetch doctor_id based on specialization
    private String getDoctorIdBySpecialization(String specialization) {
        String sql = "SELECT id FROM doctors WHERE specialization = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, specialization);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("id");
            }

        } catch (SQLException e) {
            System.out.println("[✗] Error finding doctor: " + e.getMessage());
        }
        return null;
    }

    // Check if slot is available
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
            System.out.println("[✗] Error checking availability: " + e.getMessage());
            return false;
        }
    }

    // Add patient to waitlist
    private void enqueueWaitlist(String doctorId, String date, String timeSlot, String patientId) {
        String key = buildWaitlistKey(doctorId, date, timeSlot);
        waitlists.computeIfAbsent(key, k -> new LinkedList<>()).add(patientId);
    }

    // Build waitlist key
    private String buildWaitlistKey(String doctorId, String date, String timeSlot) {
        return doctorId + "_" + date + "_" + timeSlot;
    }

    // Cancel appointment
    public boolean cancelAppointment(String appointmentId) {
        String selectSql = "SELECT * FROM appointments WHERE id = ?";
        String deleteSql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            selectStmt.setString(1, appointmentId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("[!] Appointment not found.");
                return false;
            }

            String doctorId = rs.getString("doctor_id");
            String date = rs.getString("date");
            String timeSlot = rs.getString("time_slot");

            deleteStmt.setString(1, appointmentId);
            int rowsDeleted = deleteStmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("[✓] Appointment cancelled.");
                reassignFromWaitlist(doctorId, date, timeSlot);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("[✗] Error cancelling appointment: " + e.getMessage());
        }

        return false;
    }

    // Reassign a cancelled slot to waitlisted patient
    private void reassignFromWaitlist(String doctorId, String date, String timeSlot) {
        String key = buildWaitlistKey(doctorId, date, timeSlot);
        Queue<String> queue = waitlists.get(key);

        if (queue != null && !queue.isEmpty()) {
            String nextPatientId = queue.poll();
            String newApptId = "A" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            Appointment reassigned = new Appointment(
                    newApptId,        // id
                    nextPatientId,    // patientId
                    "",               // patientName (lookup or leave blank)
                    doctorId,         // doctorId
                    "",               // issue       (original issue not stored in waitlist)
                    date,             // date
                    timeSlot,         // timeSlot
                    "Confirmed"       // status
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

    // Get all appointments with patient name
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "ORDER BY a.date, a.time_slot";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Appointment appt = new Appointment(
                        rs.getString("id"),             // id
                        rs.getString("patient_id"),     // patientId
                        rs.getString("patient_name"),   // patientName
                        rs.getString("doctor_id"),      // doctorId
                        rs.getString("issue"),          // issue
                        rs.getString("date"),           // date
                        rs.getString("time_slot"),      // timeSlot
                        rs.getString("status")          // status
                );
                list.add(appt);
            }

        } catch (SQLException e) {
            System.out.println("[✗] Error fetching appointments: " + e.getMessage());
        }

        return list;
    }

    // Get appointment by ID
    public Appointment getAppointmentById(String id) {
        String sql = "SELECT a.*, p.name AS patient_name FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id WHERE a.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Appointment(
                        rs.getString("id"),           // id
                        rs.getString("patient_id"),   // patientId
                        rs.getString("patient_name"), // patientName
                        rs.getString("doctor_id"),    // doctorId
                        rs.getString("issue"),        // issue
                        rs.getString("date"),         // date
                        rs.getString("time_slot"),    // timeSlot
                        rs.getString("status")        // status
                );
            }

        } catch (SQLException e) {
            System.out.println("[✗] Error fetching appointment: " + e.getMessage());
        }
        return null;
    }
}
