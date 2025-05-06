package com.smartclinic.model;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String contact;
    private String gender;
    private String issue;

    // Full Constructor
    public Patient(String id, String name, int age, String contact, String gender, String issue) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.gender = gender;
        this.issue = issue;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContact() { return contact; }
    public String getGender() { return gender; }
    public String getIssue() { return issue; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setContact(String contact) { this.contact = contact; }
    public void setGender(String gender) { this.gender = gender; }
    public void setIssue(String issue) { this.issue = issue; }

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
