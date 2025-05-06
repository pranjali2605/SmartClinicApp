package com.smartclinic.dao;

import com.smartclinic.model.Patient;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PatientDAO {

    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients (id, name, age, contact, gender, issue) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getId());
            stmt.setString(2, patient.getName());
            stmt.setInt(3, patient.getAge());
            stmt.setString(4, patient.getContact());
            stmt.setString(5, patient.getGender());
            stmt.setString(6, patient.getIssue());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
            return false;
        }
    }

    public Patient getPatientById(String id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Patient(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("contact"),
                        rs.getString("gender"),
                        rs.getString("issue")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving patient: " + e.getMessage());
        }
        return null;
    }

    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name = ?, age = ?, contact = ?, gender = ?, issue = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getContact());
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getIssue());
            stmt.setString(6, patient.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePatient(String id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> list = new LinkedList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("contact"),
                        rs.getString("gender"),
                        rs.getString("issue")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching patients: " + e.getMessage());
        }
        return list;
    }
}
