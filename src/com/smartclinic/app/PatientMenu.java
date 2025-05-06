package com.smartclinic.app;

import com.smartclinic.model.Patient;
import com.smartclinic.service.PatientService;

import java.util.List;
import java.util.Scanner;

public class PatientMenu {
    private static final PatientService service = new PatientService();
    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu() {
        while (true) {
            System.out.println("\n--- PATIENT MENU ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Update Patient");
            System.out.println("3. Delete Patient");
            System.out.println("4. Search Patient");
            System.out.println("5. View All Patients");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            switch (sc.nextInt()) {
                case 1 -> addPatient();
                case 2 -> updatePatient();
                case 3 -> deletePatient();
                case 4 -> searchPatient();
                case 5 -> viewAllPatients();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addPatient() {
        sc.nextLine(); // Clear buffer

        String id;
        while (true) {
            System.out.print("Enter Patient ID: ");
            id = sc.nextLine();
            if (service.getPatient(id) != null) {
                System.out.println("[✗] ID already exists. Please enter a different ID.");
            } else break;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt(); sc.nextLine();

        String contact;
        while (true) {
            System.out.print("Enter Contact (10 digits): ");
            contact = sc.nextLine();
            if (contact.matches("\\d{10}")) break;
            else System.out.println("[✗] Invalid contact number. Try again.");
        }

        System.out.print("Enter Gender (M/F/O): ");
        String gender = sc.nextLine();

        System.out.print("Enter Health Issue: ");
        String issue = sc.nextLine();

        Patient newPatient = new Patient(id, name, age, contact, gender, issue);
        if (service.addPatient(newPatient)) {
            System.out.println("[✓] Patient added.");
        } else {
            System.out.println("[✗] Failed to add.");
        }
    }

    private static void updatePatient() {
        sc.nextLine();
        System.out.print("Enter Patient ID to update: ");
        String id = sc.nextLine();
        Patient existing = service.getPatient(id);

        if (existing == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Enter new Name (" + existing.getName() + "): ");
        String name = sc.nextLine();
        name = name.isBlank() ? existing.getName() : name;

        System.out.print("Enter new Age (" + existing.getAge() + "): ");
        String ageInput = sc.nextLine();
        int age = ageInput.isBlank() ? existing.getAge() : Integer.parseInt(ageInput);

        String contact;
        while (true) {
            System.out.print("Enter new Contact (" + existing.getContact() + "): ");
            contact = sc.nextLine();
            if (contact.isBlank()) {
                contact = existing.getContact();
                break;
            } else if (contact.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("[✗] Invalid contact number. Try again.");
            }
        }

        System.out.print("Enter Gender (" + existing.getGender() + "): ");
        String gender = sc.nextLine();
        gender = gender.isBlank() ? existing.getGender() : gender;

        System.out.print("Enter Health Issue (" + existing.getIssue() + "): ");
        String issue = sc.nextLine();
        issue = issue.isBlank() ? existing.getIssue() : issue;

        Patient updated = new Patient(id, name, age, contact, gender, issue);
        if (service.updatePatient(updated)) {
            System.out.println("[✓] Patient updated.");
        } else {
            System.out.println("[✗] Update failed.");
        }
    }

    private static void deletePatient() {
        sc.nextLine();
        System.out.print("Enter Patient ID to delete: ");
        String id = sc.nextLine();
        if (service.deletePatient(id)) {
            System.out.println("[✓] Patient deleted.");
        } else {
            System.out.println("[✗] Deletion failed.");
        }
    }

    private static void searchPatient() {
        sc.nextLine();
        System.out.print("Enter Patient ID: ");
        String id = sc.nextLine();
        Patient p = service.getPatient(id);
        if (p != null) {
            printTableHeader();
            printPatientRow(p);
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void viewAllPatients() {
        List<Patient> list = service.getAllPatients();
        if (list.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            printTableHeader();
            for (Patient p : list) {
                printPatientRow(p);
            }
        }
    }

    private static void printTableHeader() {
        System.out.printf("%-10s %-15s %-5s %-12s %-8s %-20s%n", "ID", "Name", "Age", "Contact", "Gender", "Issue");
        System.out.println("--------------------------------------------------------------------------------");
    }

    private static void printPatientRow(Patient p) {
        System.out.printf("%-10s %-15s %-5d %-12s %-8s %-20s%n",
                p.getId(), p.getName(), p.getAge(), p.getContact(), p.getGender(), p.getIssue());
    }
}
