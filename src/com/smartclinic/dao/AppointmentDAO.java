package com.smartclinic.dao;

import com.smartclinic.model.Appointment;
import com.smartclinic.util.SpecializationMapper;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for managing appointments.
 * Provides methods to book, cancel, update, and fetch appointments from the database.
 */
public class AppointmentDAO {
    // Map to maintain waitlists for doctors by date and timeslot
    private final Map<String, Queue<String>> waitlists = new HashMap<>();

    /**
     * Books an appointment by checking if the selected time slot for the doctor is available.
     * If the slot is unavailable, the patient is added to the waitlist.
     * @param issue The patient's medical issue.
     * @param appt The appointment details.
     * @return boolean Returns true if the appointment was successfully booked, false otherwise.
     */
    public boolean bookAppointment(String issue, Appointment appt) {
        // Get the specialization for the issue
        String specialization = SpecializationMapper.getSpecialization(issue);
        // Fetch doctor ID based on specialization
        String doctorId = getDoctorIdBySpecialization(specialization);

        // If no doctor is found for the specialization, return false
        if (doctorId == null) {
            System.out.println("[✗] No doctor found for specialization: " + specialization);
            return false;
        }

        // Set the doctor for the appointment
        appt.setDoctorId(doctorId);

        // Check if the doctor has an available slot for the given time
        if (isSlotAvailable(doctorId, appt.getDate(), appt.getTimeSlot())) {
            // If available, save the appointment to the database
            return saveAppointment(appt);
        } else {
            // If slot is full, add patient to the waitlist
            enqueueWaitlist(doctorId, appt.getDate(), appt.getTimeSlot(), appt.getPatientId());
            System.out.println("[!] Slot is full. Patient added to waitlist.");
            return false;
        }
    }

    /**
     * Saves an appointment to the database.
     * @param appt The appointment to be saved.
     * @return boolean Returns true if the appointment was saved successfully, false otherwise.
     */
    private boolean saveAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments (id, patient_id, doctor_id, date, time_slot, issue, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appt.getId());
            stmt.setString(2, appt.getPatientId());
            stmt.setString(3, appt.getDoctorId());
            stmt.setString(4, appt.getDate());
            stmt.setString(5, appt.getTimeSlot());
            stmt.setString(6, appt.getIssue());
            stmt.setString(7, appt.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[✗] Error booking appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetches the doctor ID based on specialization.
     * @param specialization The specialization of the doctor.
     * @return String The doctor ID if found, or null if no doctor is found.
     */
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

    /**
     * Checks whether a given slot for a doctor is available.
     * @param doctorId The doctor's ID.
     * @param date The date of the appointment.
     * @param timeSlot The time slot of the appointment.
     * @return boolean Returns true if the slot is available, false otherwise.
     */
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

    /**
     * Adds a patient to the waitlist if a doctor's time slot is fully booked.
     * @param doctorId The doctor's ID.
     * @param date The date of the appointment.
     * @param timeSlot The time slot of the appointment.
     * @param patientId The patient's ID.
     */
    private void enqueueWaitlist(String doctorId, String date, String timeSlot, String patientId) {
        String key = doctorId + "_" + date + "_" + timeSlot;
        waitlists.computeIfAbsent(key, k -> new LinkedList<>()).add(patientId);
    }

    /**
     * Cancels an appointment and reassigns a patient from the waitlist to the freed slot if available.
     * @param appointmentId The ID of the appointment to be cancelled.
     * @return boolean Returns true if the appointment was cancelled successfully, false otherwise.
     */
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

            // Delete the appointment
            deleteStmt.setString(1, appointmentId);
            int rowsDeleted = deleteStmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("[✓] Appointment cancelled.");
                // Reassign the freed slot to a waitlisted patient
                reassignFromWaitlist(doctorId, date, timeSlot);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("[✗] Error cancelling appointment: " + e.getMessage());
        }
        return false;
    }

    /**
     * Reassigns a patient from the waitlist to an available slot if an appointment is cancelled.
     * @param doctorId The doctor's ID.
     * @param date The date of the cancelled appointment.
     * @param timeSlot The time slot of the cancelled appointment.
     */
    private void reassignFromWaitlist(String doctorId, String date, String timeSlot) {
        String key = doctorId + "_" + date + "_" + timeSlot;
        Queue<String> queue = waitlists.get(key);

        if (queue != null && !queue.isEmpty()) {
            String nextPatientId = queue.poll();
            String newApptId = "A" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

            Appointment reassigned = new Appointment(
                    newApptId, nextPatientId, "", doctorId, "", date, timeSlot, "Confirmed"
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

    /**
     * Retrieves all appointments from the database.
     * @return List<Appointment> A list of all appointments.
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id ORDER BY a.date, a.time_slot";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Appointment appt = new Appointment(
                        rs.getString("id"),
                        rs.getString("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_id"),
                        rs.getString("issue"),
                        rs.getString("date"),
                        rs.getString("time_slot"),
                        rs.getString("status")
                );
                list.add(appt);
            }
        } catch (SQLException e) {
            System.out.println("[✗] Error fetching appointments: " + e.getMessage());
        }

        return list;
    }

    /**
     * Retrieves an appointment by its ID.
     * @param id The ID of the appointment.
     * @return Appointment The appointment if found, null otherwise.
     */
    public Appointment getAppointmentById(String id) {
        String sql = "SELECT a.*, p.name AS patient_name FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id WHERE a.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Appointment(
                        rs.getString("id"),
                        rs.getString("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_id"),
                        rs.getString("issue"),
                        rs.getString("date"),
                        rs.getString("time_slot"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("[✗] Error fetching appointment: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates an existing appointment.
     * @param appt The updated appointment details.
     * @return boolean Returns true if the appointment was updated successfully, false otherwise.
     */
    public boolean updateAppointment(Appointment appt) {
        String sql = "UPDATE appointments SET date = ?, time_slot = ?, issue = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appt.getDate());
            stmt.setString(2, appt.getTimeSlot());
            stmt.setString(3, appt.getIssue());
            stmt.setString(4, appt.getStatus());
            stmt.setString(5, appt.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("[✓] Appointment updated successfully.");
                return true;
            } else {
                System.out.println("[!] No appointment found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("[✗] Error updating appointment: " + e.getMessage());
        }
        return false;
    }
}
