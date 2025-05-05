package com.smartclinic.model;

public class Doctor {
    private String id;
    private String name;
    private String specialization;
    private String timeSlots; // Comma-separated: "09:00,10:00,11:00"

    public Doctor(String id, String name, String specialization, String timeSlots) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.timeSlots = timeSlots;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getTimeSlots() { return timeSlots; }

    public void setName(String name) { this.name = name; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setTimeSlots(String timeSlots) { this.timeSlots = timeSlots; }

    @Override
    public String toString() {
        return "Doctor ID: " + id + ", Name: " + name + ", Specialization: " + specialization +
                ", Time Slots: " + timeSlots;
    }
}
