package com.smartclinic.service;

import com.smartclinic.model.Appointment;

import java.time.LocalDateTime;
import java.util.*;

public class AppointmentService {

    private List<Appointment> appointmentList = new LinkedList<>();

    public void bookAppointment(String id, String patient, String doctor, LocalDateTime dateTime) {
        Appointment appt = new Appointment(id, patient, doctor, dateTime);
        appointmentList.add(appt);
        System.out.println("✅ Appointment booked successfully!");
    }

    public void cancelAppointment(String id) {
        appointmentList.removeIf(appt -> appt.getAppointmentId().equals(id));
        System.out.println("🗑️ Appointment cancelled (if existed).");
    }

    public void viewAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("❗ No appointments to display.");
            return;
        }
        appointmentList.forEach(System.out::println);
    }

    // Additional methods for sorting/searching to come here
}
