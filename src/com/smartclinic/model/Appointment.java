package com.smartclinic.model;

import java.time.LocalDateTime;

public class Appointment {
    private String appointmentId;
    private String patientName;
    private String doctorName;
    private LocalDateTime dateTime;

    public Appointment(String appointmentId, String patientName, String doctorName, LocalDateTime dateTime) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
    }

    // Getters and Setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "ID='" + appointmentId + '\'' +
                ", Patient='" + patientName + '\'' +
                ", Doctor='" + doctorName + '\'' +
                ", DateTime=" + dateTime +
                '}';
    }
}
