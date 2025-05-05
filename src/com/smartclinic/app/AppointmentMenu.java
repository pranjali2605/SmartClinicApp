package com.smartclinic.app;

import com.smartclinic.model.Appointment;
import com.smartclinic.service.AppointmentService;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AppointmentMenu {
    private static final AppointmentService service = new AppointmentService();
    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu() {
        while (true) {
            System.out.println("\n--- APPOINTMENT MENU ---");
            System.out.println("1. Book Appointment");
            System.out.println("2. Cancel Appointment");
            System.out.println("3. View All Appointments");
            System.out.println("4. Search Appointments");
            System.out.println("5. Sort Appointments");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt(); sc.nextLine();
            switch (choice) {
                case 1 -> bookAppointment();
                case 2 -> cancelAppointment();
                case 3 -> viewAppointments();
                case 4 -> searchAppointments();       // NEW
                case 5 -> sortAppointments();         // NEW
                case 6 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }


    private static void bookAppointment() {
        System.out.print("Enter Patient ID: ");
        String patientId = sc.nextLine();
        System.out.print("Enter Doctor ID: ");
        String doctorId = sc.nextLine();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter Time Slot (e.g., 10:00): ");
        String time = sc.nextLine();

        String apptId = "A" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Appointment appt = new Appointment(apptId, patientId, doctorId, date, time, "Confirmed");

        if (service.bookAppointment(appt)) {
            System.out.println("[✓] Appointment Booked: " + apptId);
        } else {
            System.out.println("[!] Added to waitlist if slot was full.");
        }
    }

    private static void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        String id = sc.nextLine();

        if (service.cancelAppointment(id)) {
            System.out.println("[✓] Appointment cancelled.");
        } else {
            System.out.println("[✗] Could not cancel appointment.");
        }
    }

    private static void viewAppointments() {
        List<Appointment> list = service.getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void searchAppointments() {
        System.out.println("\nSearch By:");
        System.out.println("1. Patient ID");
        System.out.println("2. Doctor ID");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt(); sc.nextLine();

        System.out.print("Enter ID: ");
        String id = sc.nextLine();

        List<Appointment> all = service.getAllAppointments();
        boolean found = false;

        for (Appointment appt : all) {
            if ((choice == 1 && appt.getPatientId().equalsIgnoreCase(id)) ||
                    (choice == 2 && appt.getDoctorId().equalsIgnoreCase(id))) {
                System.out.println(appt);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching records found.");
        }
    }

    private static void sortAppointments() {
        System.out.println("\nSort By:");
        System.out.println("1. Patient Name (Ascending)");
        System.out.println("2. Appointment Date/Time");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt(); sc.nextLine();

        List<Appointment> all = service.getAllAppointments();

        if (choice == 1) {
            all.sort(Comparator.comparing(Appointment::getPatientId)); // Or fetch actual name via join in future
        } else if (choice == 2) {
            all.sort(Comparator.comparing(Appointment::getDate)
                    .thenComparing(Appointment::getTimeSlot));
        }

        for (Appointment appt : all) {
            System.out.println(appt);
        }
    }



}
