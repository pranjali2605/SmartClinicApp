package com.smartclinic.app;

import com.smartclinic.model.Doctor;
import com.smartclinic.service.DoctorService;

import java.util.List;
import java.util.Scanner;

public class DoctorMenu {
    private static final DoctorService service = new DoctorService();
    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu() {
        while (true) {
            System.out.println("\n--- DOCTOR MENU ---");
            System.out.println("1. Add Doctor");
            System.out.println("2. Update Doctor");
            System.out.println("3. Delete Doctor");
            System.out.println("4. Search Doctor");
            System.out.println("5. View All Doctors");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            switch (sc.nextInt()) {
                case 1 -> addDoctor();
                case 2 -> updateDoctor();
                case 3 -> deleteDoctor();
                case 4 -> searchDoctor();
                case 5 -> viewAllDoctors();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addDoctor() {
        sc.nextLine();
        System.out.print("Enter Doctor ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Specialization: ");
        String spec = sc.nextLine();
        System.out.print("Enter Available Time Slots (comma-separated): ");
        String slots = sc.nextLine();

        Doctor d = new Doctor(id, name, spec, slots);
        if (service.addDoctor(d)) {
            System.out.println("[✓] Doctor added.");
        } else {
            System.out.println("[✗] Failed to add.");
        }
    }

    private static void updateDoctor() {
        sc.nextLine();
        System.out.print("Enter Doctor ID to update: ");
        String id = sc.nextLine();
        Doctor existing = service.getDoctor(id);
        if (existing == null) {
            System.out.println("Doctor not found.");
            return;
        }

        System.out.print("New Name (" + existing.getName() + "): ");
        String name = sc.nextLine();
        name = name.isBlank() ? existing.getName() : name;

        System.out.print("New Specialization (" + existing.getSpecialization() + "): ");
        String spec = sc.nextLine();
        spec = spec.isBlank() ? existing.getSpecialization() : spec;

        System.out.print("New Time Slots (" + existing.getTimeSlots() + "): ");
        String slots = sc.nextLine();
        slots = slots.isBlank() ? existing.getTimeSlots() : slots;

        Doctor updated = new Doctor(id, name, spec, slots);
        if (service.updateDoctor(updated)) {
            System.out.println("[✓] Doctor updated.");
        } else {
            System.out.println("[✗] Update failed.");
        }
    }

    private static void deleteDoctor() {
        sc.nextLine();
        System.out.print("Enter Doctor ID to delete: ");
        String id = sc.nextLine();
        if (service.deleteDoctor(id)) {
            System.out.println("[✓] Doctor deleted.");
        } else {
            System.out.println("[✗] Deletion failed.");
        }
    }

    private static void searchDoctor() {
        sc.nextLine();
        System.out.print("Enter Doctor ID: ");
        String id = sc.nextLine();
        Doctor d = service.getDoctor(id);
        if (d != null) {
            System.out.println(d);
        } else {
            System.out.println("Doctor not found.");
        }
    }

    private static void viewAllDoctors() {
        List<Doctor> list = service.getAllDoctors();
        if (list.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            list.forEach(System.out::println);
        }
    }
}
