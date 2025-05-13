package com.smartclinic.model;

/**
 * Represents a patient in the smart clinic system.
 * Contains details about the patient such as ID, name, age, contact information, gender, and medical issue.
 */
public class Patient {

    // Patient attributes
    private final String id;           // Unique identifier for the patient
    private String name;               // Full name of the patient
    private int age;                   // Age of the patient
    private String contact;            // Contact information (e.g., phone number) of the patient
    private String gender;             // Gender of the patient (e.g., Male, Female)
    private String issue;              // Medical issue or symptoms reported by the patient

    /**
     * Constructor for creating a Patient object with all attributes.
     * @param id The unique identifier for the patient.
     * @param name The name of the patient.
     * @param age The age of the patient.
     * @param contact The contact information (e.g., phone number) of the patient.
     * @param gender The gender of the patient.
     * @param issue The medical issue or symptoms reported by the patient.
     */
    public Patient(String id, String name, int age, String contact, String gender, String issue) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.gender = gender;
        this.issue = issue;
    }

    // Getters

    /**
     * Gets the unique identifier for the patient.
     * @return The patient ID.
     */
    public String getId() { return id; }

    /**
     * Gets the name of the patient.
     * @return The patient's name.
     */
    public String getName() { return name; }

    /**
     * Gets the age of the patient.
     * @return The patient's age.
     */
    public int getAge() { return age; }

    /**
     * Gets the contact information of the patient.
     * @return The patient's contact (e.g., phone number).
     */
    public String getContact() { return contact; }

    /**
     * Gets the gender of the patient.
     * @return The patient's gender.
     */
    public String getGender() { return gender; }

    /**
     * Gets the medical issue or symptoms reported by the patient.
     * @return The patient's medical issue.
     */
    public String getIssue() { return issue; }

    // Setters

    /**
     * Sets the name of the patient.
     * @param name The name of the patient.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the age of the patient.
     * @param age The age of the patient.
     */
    public void setAge(int age) { this.age = age; }

    /**
     * Sets the contact information of the patient.
     * @param contact The contact (e.g., phone number) of the patient.
     */
    public void setContact(String contact) { this.contact = contact; }

    /**
     * Sets the gender of the patient.
     * @param gender The gender of the patient.
     */
    public void setGender(String gender) { this.gender = gender; }

    /**
     * Sets the medical issue or symptoms reported by the patient.
     * @param issue The medical issue or symptoms of the patient.
     */
    public void setIssue(String issue) { this.issue = issue; }

    /**
     * Provides a string representation of the Patient object for easy display.
     * @return A formatted string containing patient details.
     */
    @Override
    public String toString() {
        return "Patient ID: " + id +
                ", Name: " + name +
                ", Age: " + age +
                ", Contact: " + contact +
                ", Gender: " + gender +
                ", Issue: " + issue;
    }
}
