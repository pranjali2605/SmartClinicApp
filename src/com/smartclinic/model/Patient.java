package com.smartclinic.model;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String contact;

    public Patient(String id, String name, int age, String contact) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContact() { return contact; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return "Patient ID: " + id + ", Name: " + name + ", Age: " + age + ", Contact: " + contact;
    }
}
