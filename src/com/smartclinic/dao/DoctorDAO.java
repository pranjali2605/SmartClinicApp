package com.smartclinic.dao;

import com.smartclinic.model.Doctor;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DoctorDAO {

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

    public Doctor getDoctorById(String id) {
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

//    public List<Doctor> getDoctorAsListById(String id) {
//        Doctor d = getDoctorById(id);
//        if (d != null) {
//            return List.of(d);
//        } else {
//            return new LinkedList<>();
//        }
//    }

}
