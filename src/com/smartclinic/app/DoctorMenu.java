package com.smartclinic.app;

import com.smartclinic.model.Doctor;
import com.smartclinic.service.DoctorService;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                case 6 -> {
                    return;
                }
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
        System.out.print("Search by (1) ID, (2) Name, (3) Specialization: ");
        int option = sc.nextInt();
        sc.nextLine(); // consume newline

        List<Doctor> results = null;
        switch (option) {
            case 1 -> {
                System.out.print("Enter Doctor ID: ");
                String id = sc.nextLine();
                Doctor d = service.getDoctor(id);
                results = (d != null) ? List.of(d) : List.of();
            }
            case 2 -> {
                System.out.print("Enter name to search: ");
                String name = sc.nextLine().toLowerCase();
                results = service.getAllDoctors().stream()
                        .filter(doc -> doc.getName().toLowerCase().contains(name))
                        .collect(Collectors.toList());
            }
            case 3 -> {
                System.out.print("Enter specialization to search: ");
                String spec = sc.nextLine().toLowerCase();
                results = service.getAllDoctors().stream()
                        .filter(doc -> doc.getSpecialization().toLowerCase().contains(spec))
                        .collect(Collectors.toList());
            }
            default -> {
                System.out.println("Invalid option.");
                return;
            }
        }

        System.out.println("\n--- SEARCH RESULTS ---");
        printDoctorTable(results);
    }

    private static void viewAllDoctors() {
        System.out.println("\n--- ALL DOCTORS ---");
        List<Doctor> list = service.getAllDoctors();
        printDoctorTable(list);
    }

    private static void printDoctorTable(List<Doctor> doctors) {
        printDoctorTableHeader();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.\n");
            return;
        }
        for (Doctor doc : doctors) {
            printDoctorRow(doc);
        }
    }

    private static void printDoctorTableHeader() {
        System.out.printf("%-10s %-20s %-20s %-30s%n", "ID", "Name", "Specialization", "Time Slots");
        System.out.println("=".repeat(85));
    }

    private static void printDoctorRow(Doctor doc) {
        System.out.printf("%-10s %-20s %-20s %-30s%n",
                doc.getId(), doc.getName(), doc.getSpecialization(), doc.getTimeSlots());
    }
}
