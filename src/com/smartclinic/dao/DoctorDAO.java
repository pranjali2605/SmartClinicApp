package com.smartclinic.dao;

import com.smartclinic.model.Doctor;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Data Access Object (DAO) class for handling CRUD operations related to doctors.
 * Provides methods to add, update, delete, and fetch doctors from the database.
 */
public class DoctorDAO {

    /**
     * Adds a new doctor to the database.
     * @param doctor The doctor object containing the details to be added.
     * @return boolean True if the doctor was successfully added, false otherwise.
     */
    public boolean addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (id, name, specialization, time_slots) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getId());
            stmt.setString(2, doctor.getName());
            stmt.setString(3, doctor.getSpecialization());
            stmt.setString(4, doctor.getTimeSlots());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the details of an existing doctor in the database.
     * @param doctor The doctor object containing the updated details.
     * @return boolean True if the doctor details were successfully updated, false otherwise.
     */
    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET name = ?, specialization = ?, time_slots = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getTimeSlots());
            stmt.setString(4, doctor.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a doctor from the database.
     * @param id The ID of the doctor to be deleted.
     * @return boolean True if the doctor was successfully deleted, false otherwise.
     */
    public boolean deleteDoctor(String id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a doctor from the database by their ID.
     * @param id The ID of the doctor to retrieve.
     * @return Doctor The doctor object, or null if no doctor is found with the given ID.
     */
    public static Doctor getDoctorById(String id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Doctor(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("time_slots"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching doctor: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all doctors from the database.
     * @return List<Doctor> A list of all doctor objects in the database.
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new LinkedList<>();
        String sql = "SELECT * FROM doctors";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Doctor(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("time_slots")));
            }

        } catch (SQLException e) {
            System.out.println("Error listing doctors: " + e.getMessage());
        }

        return list;
    }
}
