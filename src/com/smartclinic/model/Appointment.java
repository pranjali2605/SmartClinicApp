package com.smartclinic.model;

/**
 * Represents an appointment in the smart clinic system.
 * Contains details about the appointment such as patient, doctor, issue, date, time, and status.
 */
public class Appointment {

    // Appointment attributes
    private String id;          // Unique identifier for the appointment
    private String patientId;   // Patient's unique identifier
    private String patientName; // Patient's full name
    private String doctorId;    // Doctor's unique identifier
    private String issue;       // The medical issue the patient is seeing the doctor for
    private String date;        // The date of the appointment
    private String timeSlot;    // The time slot of the appointment
    private String status;      // Status of the appointment (e.g., scheduled, completed, canceled)

    /**
     * Default constructor for the Appointment class.
     * Initializes an empty appointment object.
     */
    public Appointment() {}

    /**
     * Full constructor for creating an Appointment object with all attributes.
     * @param id The unique identifier for the appointment.
     * @param patientId The unique identifier for the patient.
     * @param patientName The name of the patient.
     * @param doctorId The unique identifier for the doctor.
     * @param issue The medical issue of the patient.
     * @param date The date of the appointment.
     * @param timeSlot The time slot of the appointment.
     * @param status The current status of the appointment.
     */
    public Appointment(String id, String patientId, String patientName, String doctorId, String issue, String date, String timeSlot, String status) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.issue = issue;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    // Getters

    /**
     * Gets the unique identifier for the appointment.
     * @return String representing the appointment ID.
     */
    public String getId() { return id; }

    /**
     * Gets the unique identifier for the patient.
     * @return String representing the patient ID.
     */
    public String getPatientId() { return patientId; }

    /**
     * Gets the name of the patient.
     * @return String representing the patient's name.
     */
    public String getPatientName() { return patientName; }

    /**
     * Gets the unique identifier for the doctor.
     * @return String representing the doctor ID.
     */
    public String getDoctorId() { return doctorId; }

    /**
     * Gets the medical issue of the patient.
     * @return String representing the issue.
     */
    public String getIssue() { return issue; }

    /**
     * Gets the date of the appointment.
     * @return String representing the date of the appointment.
     */
    public String getDate() { return date; }

    /**
     * Gets the time slot of the appointment.
     * @return String representing the time slot.
     */
    public String getTimeSlot() { return timeSlot; }

    /**
     * Gets the status of the appointment.
     * @return String representing the status.
     */
    public String getStatus() { return status; }

    // Setters

    /**
     * Sets the unique identifier for the appointment.
     * @param id The unique identifier for the appointment.
     */
    public void setId(String id) { this.id = id; }

    /**
     * Sets the unique identifier for the doctor.
     * @param doctorId The unique identifier for the doctor.
     */
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    /**
     * Provides a string representation of the Appointment object for easy display.
     * @return A formatted string containing appointment details.
     */
    @Override
    public String toString() {
        return "Appointment ID: " + id +
                ", Patient: " + patientName + " (ID: " + patientId + ")" +
                ", Doctor: " + doctorId +
                ", Issue: " + issue +
                ", Date: " + date +
                ", Time: " + timeSlot +
                ", Status: " + status;
    }
}
