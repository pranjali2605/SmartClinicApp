package com.smartclinic.app;

import javax.swing.*;
import java.awt.*;

/**
 * The SmartClinicApp class represents the main GUI for the Smart Clinic application.
 * This screen acts as a starting point for navigating between different functionalities
 * such as managing patients, doctors, appointments, and exiting the application.
 */
public class SmartClinicApp extends JFrame {

    /**
     * Static method to open the main menu of the application.
     * This method ensures the GUI is initialized on the Swing Event Dispatch Thread (EDT).
     *
     * @see SwingUtilities#invokeLater(Runnable)
     */
    public static void openMainMenu() {
        SwingUtilities.invokeLater(SmartClinicApp::new);
    }

    /**
     * Constructor to initialize and set up the main menu GUI.
     * The constructor configures the window's layout, buttons, and actions.
     */
    public SmartClinicApp() {
        // Set the title of the window
        setTitle("Smart Clinic - Main Menu");

        // Close the application when the window is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set the size of the window
        setSize(400, 300);

        // Use GridLayout for organizing buttons and labels
        setLayout(new GridLayout(5, 1, 10, 10));

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Title label in the center of the window
        JLabel title = new JLabel("SMART CLINIC MAIN MENU", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18)); // Set font for the title

        // Create buttons for different functionalities
        JButton patientBtn = new JButton("Manage Patients");
        JButton doctorBtn = new JButton("Manage Doctors");
        JButton appointmentBtn = new JButton("Manage Appointments");
        JButton exitBtn = new JButton("Exit");

        // Add action listeners to the buttons
        // When the button is clicked, respective menus are opened
        patientBtn.addActionListener(e -> PatientMenu.showMenuGUI());
        doctorBtn.addActionListener(e -> DoctorMenu.showMenuGUI());
        appointmentBtn.addActionListener(e -> AppointmentMenu.showMenuGUI()); // Uncomment when ready
        exitBtn.addActionListener(e -> System.exit(0)); // Exit the application

        // Add components to the window (title and buttons)
        add(title);
        add(patientBtn);
        add(doctorBtn);
        add(appointmentBtn);
        add(exitBtn);

        // Make the window visible
        setVisible(true);
    }

    /**
     * Main method to launch the application.
     * This method ensures the GUI is created on the Swing Event Dispatch Thread (EDT).
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Ensure the main menu is opened on the EDT
        SwingUtilities.invokeLater(SmartClinicApp::new);
    }
}
