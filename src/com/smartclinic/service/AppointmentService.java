package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDAO;
import com.smartclinic.dao.DoctorDAO;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.util.SpecializationMapper;

import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    // Book appointment using issue
    public boolean bookAppointment(String issue, Appointment appt) {
        return appointmentDAO.bookAppointment(issue, appt);
    }

    public boolean cancelAppointment(String id) {
        return appointmentDAO.cancelAppointment(id);
    }

    public Appointment getAppointment(String id) {
        return appointmentDAO.getAppointmentById(id);
    }

    public Appointment getAppointmentById(String apptId) {
        return appointmentDAO.getAppointmentById(apptId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    public List<Doctor> suggestDoctorsByIssue(String issue) {
        String specialization = SpecializationMapper.getSpecialization(issue);
        if (specialization == null) return List.of();

        return doctorDAO.getAllDoctors().stream()
                .filter(doc -> specialization.equalsIgnoreCase(doc.getSpecialization()))
                .collect(Collectors.toList());
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorDAO.getAllDoctors().stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }

    public boolean isDoctorAvailable(String doctorId, String date, String timeSlot) {
        List<Appointment> appointments = getAppointmentsByDoctorId(doctorId);
        return appointments.stream().noneMatch(a ->
                a.getDate().equals(date) && a.getTimeSlot().equals(timeSlot)
        );
    }

    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return appointmentDAO.getAllAppointments().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public boolean isTimeSlotValid(Doctor doctor, String timeSlot) {
        List<String> slots = Arrays.asList(doctor.getTimeSlots().split(","));
        return slots.contains(timeSlot);
    }

    // 🔍 Binary search by patient name (case-insensitive)
    public Appointment searchByPatientName(String name) {
        List<Appointment> list = new LinkedList<>(getAllAppointments());
        list.sort(Comparator.comparing(Appointment::getPatientName, String.CASE_INSENSITIVE_ORDER));
        return binarySearchByPatientName(list, name.toLowerCase().trim());
    }

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

    // 🔍 Linear search by any field (case-insensitive)
    public List<Appointment> searchAppointmentsByAnyField(String query) {
        final String lowerQuery = query.toLowerCase().trim(); // ✅ effectively final
        return getAllAppointments().stream()
                .filter(a -> a.getPatientName().toLowerCase().contains(lowerQuery)
                        || a.getDoctorId().toLowerCase().contains(lowerQuery)
                        || a.getDate().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    // 🔃 Sort appointments by date and time slot
    public List<Appointment> getSortedAppointments() {
        List<Appointment> sorted = new LinkedList<>(getAllAppointments());
        sorted.sort(Comparator.comparing(Appointment::getDate)
                .thenComparing(Appointment::getTimeSlot));
        return sorted;
    }

    // ✅ Update appointment (assumes DAO has update logic)
    public boolean updateAppointment(Appointment updatedAppt) {
        return appointmentDAO.updateAppointment(updatedAppt);
    }

    // ✅ Delete appointment by ID (alternative name)
    public boolean deleteAppointment(String apptId) {
        return cancelAppointment(apptId);
    }

    // ⬅️ Placeholder for "Back to Main Menu" (UI implementation needed)
    public void backToMainMenu() {
        System.out.println("Returning to main menu...");
        // Logic to call menu should be handled in your Menu class
    }
}
