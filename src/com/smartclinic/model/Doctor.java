package com.smartclinic.model;

public class Doctor {
    private String id;
    private String name;
    private String specialization;
    private String timeSlots; // Comma-separated: "09:00,10:00,11:00"

    public Doctor(String id, String name, String specialization, String timeSlots) {
        setId(id);
        setName(name);
        setSpecialization(specialization);
        setTimeSlots(timeSlots);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getTimeSlots() { return timeSlots; }

    // Setters with validation
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor ID cannot be empty.");
        }
        this.id = id.trim();
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be empty.");
        }
        this.name = name.trim();
    }

    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty.");
        }
        this.specialization = specialization.trim();
    }

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

        this.timeSlots = String.join(",", slots).trim();
    }


    @Override
    public String toString() {
        return "Doctor ID: " + id +
                ", Name: " + name +
                ", Specialization: " + specialization +
                ", Time Slots: " + timeSlots;
    }
}
