package com.smartclinic.app;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.util.SpecializationMapper;

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
                case 4 -> searchAppointments();
                case 5 -> sortAppointments();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void bookAppointment() {

        System.out.print("Enter patient ID: ");
        String patientId = sc.nextLine();

        System.out.print("Enter patient name: ");
        String patientName = sc.nextLine();

        System.out.print("Enter issue: ");
        String issue = sc.nextLine();

        String specialization = SpecializationMapper.getSpecialization(issue);
        List<Doctor> matchedDoctors = service.suggestDoctorsByIssue(issue); // uses specialization internally

        if (matchedDoctors.isEmpty()) {
            System.out.println("No doctors available for this issue/specialization.");
            return;
        }

        System.out.println("Available Doctors:");
        for (int i = 0; i < matchedDoctors.size(); i++) {
            Doctor doc = matchedDoctors.get(i);
            System.out.printf("%d. %s (ID: %s, Specialization: %s)%n", i + 1, doc.getName(), doc.getId(), doc.getSpecialization());
        }

        System.out.print("Select doctor (enter number): ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        if (choice < 1 || choice > matchedDoctors.size()) {
            System.out.println("Invalid doctor selection.");
            return;
        }

        Doctor selectedDoctor = matchedDoctors.get(choice - 1);

        System.out.println("Available Time Slots: " + selectedDoctor.getTimeSlots());
        System.out.print("Enter preferred date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        System.out.print("Enter preferred time slot (e.g., 10:00): ");
        String timeSlot = sc.nextLine();

        // Validate time slot and availability (assumes you have these methods)
        if (!service.isTimeSlotValid(selectedDoctor, timeSlot)) {
            System.out.println("Invalid time slot for selected doctor.");
            return;
        }

        if (!service.isDoctorAvailable(selectedDoctor.getId(), date, timeSlot)) {
            System.out.println("Selected doctor is not available at this time.");
            return;
        }

        // Create appointment
        String apptId = "A" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Appointment appt = new Appointment(apptId, patientId, patientName, selectedDoctor.getId(), issue, date, timeSlot, "Confirmed");


        if (service.bookAppointment(issue, appt)) {
            System.out.println("[✓] Appointment booked successfully: " + apptId);
        } else {
            System.out.println("[!] Could not book appointment. Try again later.");
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
            printAppointmentTable(list);
        }
    }

    private static void searchAppointments() {
        System.out.println("\nSearch By:");
        System.out.println("1. Patient Name");
        System.out.println("2. Doctor ID");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt(); sc.nextLine();

        System.out.print("Enter value: ");
        String input = sc.nextLine();

        List<Appointment> all = service.getAllAppointments();
        List<Appointment> filtered = all.stream()
                .filter(appt -> (choice == 1 && appt.getPatientName().equalsIgnoreCase(input)) ||
                        (choice == 2 && appt.getDoctorId().equalsIgnoreCase(input)))
                .toList();


        if (filtered.isEmpty()) {
            System.out.println("No matching records found.");
        } else {
            printAppointmentTable(filtered);
        }
    }

    private static void sortAppointments() {
        System.out.println("\nSort By:");
        System.out.println("1. Patient ID");
        System.out.println("2. Appointment Date/Time");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt(); sc.nextLine();

        List<Appointment> all = service.getAllAppointments();

        if (choice == 1) {
            all.sort(Comparator.comparing(Appointment::getPatientName));
        }
        else if (choice == 2) {
            all.sort(Comparator.comparing(Appointment::getDate)
                    .thenComparing(Appointment::getTimeSlot));
        }

        if (all.isEmpty()) {
            System.out.println("No appointments to sort.");
        } else {
            printAppointmentTable(all);
        }
    }

    private static void printAppointmentTable(List<Appointment> list) {
        System.out.printf("%-10s %-15s %-12s %-15s %-12s %-10s %-12s%n",
                "Appt ID", "Patient Name", "Doctor ID", "Issue", "Date", "Time", "Status");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Appointment appt : list) {
            System.out.printf("%-10s %-15s %-12s %-15s %-12s %-10s %-12s%n",
                    appt.getId(),
                    appt.getPatientName(),
                    appt.getDoctorId(),
                    appt.getIssue(),
                    appt.getDate(),
                    appt.getTimeSlot(),
                    appt.getStatus());
        }

    }
}
