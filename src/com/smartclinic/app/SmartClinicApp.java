package com.smartclinic.app;

import java.util.Scanner;

public class SmartClinicApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n==========================");
            System.out.println("  SMART CLINIC MAIN MENU");
            System.out.println("==========================");
            System.out.println("1. Manage Patients");
            System.out.println("2. Manage Doctors");
            System.out.println("3. Manage Appointments");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> PatientMenu.showMenu();
                case 2 -> DoctorMenu.showMenu();
                case 3 -> AppointmentMenu.showMenu();
                case 4 -> {
                    System.out.println("Exiting system. Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
