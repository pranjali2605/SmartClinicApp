package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDAO;
import com.smartclinic.dao.DoctorDAO;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.util.SpecializationMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

//    public boolean bookAppointment(Appointment appt) {
//        return appointmentDAO.bookAppointment(appt);
//    }
    public boolean bookAppointment(String issue, Appointment appt) {
        return appointmentDAO.bookAppointment(issue, appt);
    }

    public boolean cancelAppointment(String id) {
        return appointmentDAO.cancelAppointment(id);
    }

    public Appointment getAppointment(String id) {
        return appointmentDAO.getAppointmentById(id);
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
}
