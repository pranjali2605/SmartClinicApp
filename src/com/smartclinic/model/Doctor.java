package com.smartclinic.model;

/**
 * Represents a doctor in the smart clinic system.
 * Contains details about the doctor such as ID, name, specialization, and available time slots.
 */
public class Doctor {

    // Doctor attributes
    private String id;            // Unique identifier for the doctor
    private String name;          // Doctor's full name
    private String specialization; // Doctor's medical specialization (e.g., Cardiologist, Dermatologist)
    private String timeSlots;     // Available time slots for appointments, comma-separated (e.g., "09:00,10:00,11:00")

    /**
     * Constructor for creating a Doctor object with all attributes.
     * @param id The unique identifier for the doctor.
     * @param name The name of the doctor.
     * @param specialization The specialization of the doctor.
     * @param timeSlots A comma-separated string representing the available time slots (e.g., "09:00,10:00,11:00").
     */
    public Doctor(String id, String name, String specialization, String timeSlots) {
        setId(id);
        setName(name);
        setSpecialization(specialization);
        setTimeSlots(timeSlots);
    }

    // Getters

    /**
     * Gets the unique identifier for the doctor.
     * @return String representing the doctor ID.
     */
    public String getId() { return id; }

    /**
     * Gets the name of the doctor.
     * @return String representing the doctor's name.
     */
    public String getName() { return name; }

    /**
     * Gets the specialization of the doctor.
     * @return String representing the doctor's specialization.
     */
    public String getSpecialization() { return specialization; }

    /**
     * Gets the available time slots for the doctor.
     * @return A comma-separated string representing the time slots.
     */
    public String getTimeSlots() { return timeSlots; }

    // Setters with validation

    /**
     * Sets the unique identifier for the doctor.
     * @param id The unique identifier for the doctor.
     * @throws IllegalArgumentException if the ID is null or empty.
     */
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor ID cannot be empty.");
        }
        this.id = id.trim();
    }

    /**
     * Sets the name of the doctor.
     * @param name The name of the doctor.
     * @throws IllegalArgumentException if the name is null or empty.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be empty.");
        }
        this.name = name.trim();
    }

    /**
     * Sets the specialization of the doctor.
     * @param specialization The specialization of the doctor.
     * @throws IllegalArgumentException if the specialization is null or empty.
     */
    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty.");
        }
        this.specialization = specialization.trim();
    }

    /**
     * Sets the available time slots for the doctor.
     * @param timeSlots A comma-separated string representing the time slots (e.g., "09:00,10:00,11:00").
     * @throws IllegalArgumentException if the time slots string is null, empty, or contains invalid time formats.
     */
    public void setTimeSlots(String timeSlots) {
        if (timeSlots == null || timeSlots.trim().isEmpty()) {
            throw new IllegalArgumentException("Time slots cannot be empty.");
        }

        // Validate each time slot format (HH:mm)
        String[] slots = timeSlots.split(",");
        for (String slot : slots) {
            if (!slot.trim().matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                throw new IllegalArgumentException("Invalid time format in slots. Use HH:mm.");
            }
        }

        // Join valid slots and trim the string
        this.timeSlots = String.join(",", slots).trim();
    }

    /**
     * Provides a string representation of the Doctor object for easy display.
     * @return A formatted string containing doctor details.
     */
    @Override
    public String toString() {
        return "Doctor ID: " + id +
                ", Name: " + name +
                ", Specialization: " + specialization +
                ", Time Slots: " + timeSlots;
    }
}
