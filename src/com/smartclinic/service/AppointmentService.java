package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDAO;
import com.smartclinic.model.Appointment;

import java.util.List;

public class AppointmentService {
    private final AppointmentDAO dao = new AppointmentDAO();

    public boolean bookAppointment(Appointment appt) {
        return dao.bookAppointment(appt);
    }

    public boolean cancelAppointment(String id) {
        return dao.cancelAppointment(id);
    }

    public Appointment getAppointment(String id) {
        return dao.getAppointmentById(id);
    }

    public List<Appointment> getAllAppointments() {
        return dao.getAllAppointments();
    }
}
