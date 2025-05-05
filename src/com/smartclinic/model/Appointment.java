package com.smartclinic.model;

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private String date;
    private String timeSlot;
    private String status; // e.g., "Confirmed", "Waiting"

    public Appointment(String id, String patientId, String doctorId, String date, String timeSlot, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Appointment ID: " + id + ", Patient: " + patientId + ", Doctor: " + doctorId +
                ", Date: " + date + ", Time: " + timeSlot + ", Status: " + status;
    }
}
