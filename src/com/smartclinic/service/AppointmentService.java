package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDAO;
import com.smartclinic.dao.DoctorDAO;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.util.SpecializationMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class that handles operations related to appointments.
 * It interacts with the DAO layer to fetch and update data and provides business logic
 * such as booking, canceling, searching, and sorting appointments.
 */
public class AppointmentService {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();  // Data Access Object for appointments
    private final DoctorDAO doctorDAO = new DoctorDAO();  // Data Access Object for doctors

    /**
     * Books an appointment based on the patient's medical issue.
     * @param issue The medical issue for which the appointment is to be booked.
     * @param appt The Appointment object containing appointment details.
     * @return true if the appointment was successfully booked, false otherwise.
     */
    public boolean bookAppointment(String issue, Appointment appt) {
        return appointmentDAO.bookAppointment(issue, appt);
    }

    /**
     * Cancels an existing appointment by its ID.
     * @param id The ID of the appointment to be canceled.
     * @return true if the appointment was successfully canceled, false otherwise.
     */
    public boolean cancelAppointment(String id) {
        return appointmentDAO.cancelAppointment(id);
    }

    /**
     * Retrieves an appointment by its ID.
     * @param id The ID of the appointment to retrieve.
     * @return The Appointment object with the given ID, or null if not found.
     */
    public Appointment getAppointment(String id) {
        return appointmentDAO.getAppointmentById(id);
    }

    /**
     * Retrieves all appointments in the system.
     * @return A list of all appointments.
     */
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    /**
     * Suggests a list of doctors based on the patient's medical issue.
     * Uses the SpecializationMapper to map the issue to a doctor specialization.
     * @param issue The medical issue to map to a specialization.
     * @return A list of doctors that specialize in the given issue.
     */
    public List<Doctor> suggestDoctorsByIssue(String issue) {
        String specialization = SpecializationMapper.getSpecialization(issue);
        if (specialization == null) return List.of(); // Return an empty list if no specialization is found

        return doctorDAO.getAllDoctors().stream()
                .filter(doc -> specialization.equalsIgnoreCase(doc.getSpecialization()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of doctors who specialize in a given specialization.
     * @param specialization The specialization to filter doctors by.
     * @return A list of doctors who match the given specialization.
     */
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorDAO.getAllDoctors().stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a doctor is available for a specific time slot on a specific date.
     * @param doctorId The ID of the doctor to check.
     * @param date The date to check availability.
     * @param timeSlot The time slot to check availability.
     * @return true if the doctor is available, false otherwise.
     */
    public boolean isDoctorAvailable(String doctorId, String date, String timeSlot) {
        List<Appointment> appointments = getAppointmentsByDoctorId(doctorId);
        return appointments.stream().noneMatch(a -> a.getDate().equals(date) && a.getTimeSlot().equals(timeSlot));
    }

    /**
     * Retrieves all appointments for a specific doctor.
     * @param doctorId The ID of the doctor to filter appointments by.
     * @return A list of appointments for the specified doctor.
     */
    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return appointmentDAO.getAllAppointments().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a time slot is valid for a specific doctor based on their available time slots.
     * @param doctor The Doctor object containing the available time slots.
     * @param timeSlot The time slot to check for validity.
     * @return true if the time slot is valid, false otherwise.
     */
    public boolean isTimeSlotValid(Doctor doctor, String timeSlot) {
        List<String> slots = Arrays.asList(doctor.getTimeSlots().split(","));
        return slots.contains(timeSlot);
    }

    /**
     * Searches for an appointment based on the patient's name using binary search.
     * @param name The patient's name to search for.
     * @return The Appointment object if found, null otherwise.
     */
    public Appointment searchByPatientName(String name) {
        List<Appointment> list = new LinkedList<>(getAllAppointments());
        list.sort(Comparator.comparing(Appointment::getPatientName, String.CASE_INSENSITIVE_ORDER));
        return binarySearchByPatientName(list, name.toLowerCase().trim());
    }

    /**
     * Performs a binary search on a sorted list of appointments based on the patient's name.
     * @param list The list of appointments to search through.
     * @param target The target name to search for.
     * @return The Appointment object if found, null otherwise.
     */
    private Appointment binarySearchByPatientName(List<Appointment> list, String target) {
        int low = 0, high = list.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            String current = list.get(mid).getPatientName().toLowerCase();
            int cmp = current.compareTo(target);
            if (cmp == 0) return list.get(mid);
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    /**
     * Performs a linear search for appointments by any field (patient name, doctor ID, or date).
     * @param query The query string to search for (case-insensitive).
     * @return A list of appointments matching the query.
     */
    public List<Appointment> searchAppointmentsByAnyField(String query) {
        final String lowerQuery = query.toLowerCase().trim(); // Effectively final
        return getAllAppointments().stream()
                .filter(a -> a.getPatientName().toLowerCase().contains(lowerQuery)
                        || a.getDoctorId().toLowerCase().contains(lowerQuery)
                        || a.getDate().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Sorts all appointments by date and time slot.
     * @return A sorted list of appointments.
     */
    public List<Appointment> getSortedAppointments() {
        List<Appointment> sorted = new LinkedList<>(getAllAppointments());
        sorted.sort(Comparator.comparing(Appointment::getDate)
                .thenComparing(Appointment::getTimeSlot));
        return sorted;
    }

    /**
     * Updates an existing appointment.
     * @param updatedAppt The updated Appointment object.
     * @return true if the appointment was successfully updated, false otherwise.
     */
    public boolean updateAppointment(Appointment updatedAppt) {
        return appointmentDAO.updateAppointment(updatedAppt);
    }

    /**
     * Deletes an appointment by its ID.
     * @param apptId The ID of the appointment to delete.
     * @return true if the appointment was successfully deleted, false otherwise.
     */
    public boolean deleteAppointment(String apptId) {
        return cancelAppointment(apptId);
    }

    /**
     * Placeholder method for going back to the main menu.
     * This is UI-specific and will require integration with the UI logic.
     */
    public void backToMainMenu() {
        System.out.println("Returning to main menu...");
        // Logic to call menu should be handled in your Menu class
    }
}
