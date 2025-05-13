package com.smartclinic.app;

import javax.swing.*;
import java.awt.*;

public class SmartClinicApp extends JFrame {

    // ✅ Static method placed correctly
    public static void openMainMenu() {
        SwingUtilities.invokeLater(SmartClinicApp::new);
    }

    public SmartClinicApp() {
        setTitle("Smart Clinic - Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(5, 1, 10, 10));
        setLocationRelativeTo(null);

        JLabel title = new JLabel("SMART CLINIC MAIN MENU", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton patientBtn = new JButton("Manage Patients");
        JButton doctorBtn = new JButton("Manage Doctors");
        JButton appointmentBtn = new JButton("Manage Appointments");
        JButton exitBtn = new JButton("Exit");

        patientBtn.addActionListener(e -> PatientMenu.showMenuGUI());
        doctorBtn.addActionListener(e -> DoctorMenu.showMenuGUI());
         appointmentBtn.addActionListener(e -> AppointmentMenu.showMenuGUI()); // Uncomment when ready
        exitBtn.addActionListener(e -> System.exit(0));

        add(title);
        add(patientBtn);
        add(doctorBtn);
        add(appointmentBtn);
        add(exitBtn);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartClinicApp::new);
    }
}
