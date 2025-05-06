package com.smartclinic.model;

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private String issue;
    private String date;
    private String timeSlot;
    private String status; // e.g., "Confirmed", "Waiting"

    public Appointment(String id, String patientId, String doctorId, String issue, String date, String timeSlot, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.issue = issue;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getIssue() { return issue; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setIssue(String issue) { this.issue = issue; }
    public void setDate(String date) { this.date = date; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public void setStatus(String status) { this.status = status; }
}
