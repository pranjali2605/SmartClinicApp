package com.smartclinic.app;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.Patient;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.PatientService;
import com.smartclinic.util.SpecializationMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AppointmentMenu is the class responsible for displaying the Appointment Management GUI.
 * It provides functionalities for viewing, searching, sorting, booking, and canceling appointments.
 */
public class AppointmentMenu {

    // Services to interact with the appointment and patient data
    private static final AppointmentService apptService = new AppointmentService();
    private static final PatientService patientService = new PatientService();

    /**
     * Initializes and displays the Appointment Management menu.
     */
    public static void showMenuGUI() {
        // JFrame setup for the GUI
        JFrame frame = new JFrame("Appointment Management");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Table setup for displaying appointments
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Appt ID", "Patient", "Doctor ID", "Issue", "Date", "Time", "Status"}, 0);
        JTable table = new JTable(model);
        refreshTable(model); // Populate the table with current appointments

        // Panel for search and sorting options
        JPanel searchSortPanel = new JPanel();
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton sortBtn = new JButton("Sort by Date");

        searchSortPanel.add(new JLabel("Search:"));
        searchSortPanel.add(searchField);
        searchSortPanel.add(searchBtn);
        searchSortPanel.add(sortBtn);

        // Action listeners for search and sorting functionality
        searchBtn.addActionListener(e -> searchAppointments(searchField.getText(), model));
        sortBtn.addActionListener(e -> sortAppointments(model));

        // Panel for buttons like booking, canceling, and refreshing
        JPanel buttonPanel = new JPanel();
        JButton bookBtn = new JButton("Book Appointment");
        JButton cancelBtn = new JButton("Cancel Appointment");
        JButton refreshBtn = new JButton("View All");
        JButton backToMainBtn = new JButton("Back to Main Menu");

        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backToMainBtn);

        // Button actions for appointment booking, canceling, and refreshing
        bookBtn.addActionListener(e -> showBookingForm(model));

        cancelBtn.addActionListener(e -> {
            // Cancel selected appointment
            int row = table.getSelectedRow();
            if (row != -1) {
                String apptId = (String) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(frame, "Cancel appointment " + apptId + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    if (apptService.cancelAppointment(apptId)) {
                        JOptionPane.showMessageDialog(frame, "Cancelled.");
                        refreshTable(model); // Refresh table after cancellation
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to cancel.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select an appointment to cancel.");
            }
        });

        // Refresh the table with all appointments
        refreshBtn.addActionListener(e -> refreshTable(model));

        // Go back to the main menu
        backToMainBtn.addActionListener(e -> {
            frame.dispose(); // Close current window
            SmartClinicApp.openMainMenu(); // Return to main menu
        });

        // Frame layout
        frame.add(searchSortPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Refreshes the table to show the latest appointments.
     *
     * @param model The table model to refresh
     */
    private static void refreshTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear current table content
        List<Appointment> list = apptService.getAllAppointments(); // Retrieve all appointments
        for (Appointment a : list) {
            model.addRow(new Object[]{
                    a.getId(), a.getPatientName(), a.getDoctorId(),
                    a.getIssue(), a.getDate(), a.getTimeSlot(), a.getStatus()
            });
        }
    }

    /**
     * Searches for appointments based on a query (case-insensitive).
     *
     * @param query The search query (patient, doctor, or date)
     * @param model The table model to update with search results
     */
    private static void searchAppointments(String query, DefaultTableModel model) {
        model.setRowCount(0); // Clear current table

        List<Appointment> list = apptService.getAllAppointments();
        String lowerQuery = query.toLowerCase(); // Case-insensitive search

        // Filter appointments based on query
        List<Appointment> filteredList = list.stream()
                .filter(a -> a.getPatientName().toLowerCase().contains(lowerQuery) ||
                        a.getDoctorId().toLowerCase().contains(lowerQuery) ||
                        a.getDate().toLowerCase().contains(lowerQuery))
                .toList();

        for (Appointment a : filteredList) {
            model.addRow(new Object[]{
                    a.getId(), a.getPatientName(), a.getDoctorId(),
                    a.getIssue(), a.getDate(), a.getTimeSlot(), a.getStatus()
            });
        }

        if (filteredList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No appointments found.");
        }
    }

    /**
     * Sorts the appointments by date in ascending order.
     *
     * @param model The table model to update with sorted appointments
     */
    private static void sortAppointments(DefaultTableModel model) {
        model.setRowCount(0); // Clear current table
        List<Appointment> list = apptService.getAllAppointments();

        // Sort appointments by date
        list.sort(Comparator.comparing(Appointment::getDate));

        for (Appointment a : list) {
            model.addRow(new Object[]{
                    a.getId(), a.getPatientName(), a.getDoctorId(),
                    a.getIssue(), a.getDate(), a.getTimeSlot(), a.getStatus()
            });
        }
    }

    /**
     * Displays the appointment booking form where users can select patient, doctor, date, and time.
     *
     * @param model The table model to refresh after booking an appointment
     */
    private static void showBookingForm(DefaultTableModel model) {
        String patientId = JOptionPane.showInputDialog("Enter Patient ID:");
        if (patientId == null || patientId.trim().isEmpty()) return;

        Patient p = patientService.getPatient(patientId.trim());
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Patient not found.");
            return;
        }

        String issue = p.getIssue();
        String specialization = SpecializationMapper.getSpecialization(issue);
        List<Doctor> matchedDoctors = apptService.suggestDoctorsByIssue(issue);

        if (matchedDoctors.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No doctors available for the issue: " + issue);
            return;
        }

        String patientDetails = "Patient Details:\n" +
                "Name: " + p.getName() + "\n" +
                "Issue: " + issue + "\n" +
                "Matching Specialization: " + specialization + "\n\n";

        String[] doctorOptions = matchedDoctors.stream()
                .map(doc -> doc.getName() + " (ID: " + doc.getId() + ") | " + doc.getSpecialization())
                .toArray(String[]::new);

        String selectedDoctorStr = (String) JOptionPane.showInputDialog(
                null,
                patientDetails + "Select a doctor:",
                "Choose Doctor",
                JOptionPane.PLAIN_MESSAGE,
                null,
                doctorOptions,
                doctorOptions[0]);

        if (selectedDoctorStr == null) return;

        Doctor selectedDoctor = matchedDoctors.stream()
                .filter(doc -> selectedDoctorStr.contains(doc.getId()))
                .findFirst()
                .orElse(null);

        if (selectedDoctor == null) {
            JOptionPane.showMessageDialog(null, "Doctor selection failed.");
            return;
        }

        String timeSlots = selectedDoctor.getTimeSlots();
        String date = JOptionPane.showInputDialog("Enter preferred date (YYYY-MM-DD):");
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Invalid date format.");
            return;
        }

        String timeSlot = JOptionPane.showInputDialog("Enter time slot (Available: " + timeSlots + "):");
        if (timeSlot == null || !apptService.isTimeSlotValid(selectedDoctor, timeSlot)) {
            JOptionPane.showMessageDialog(null, "Invalid time slot.");
            return;
        }

        if (!apptService.isDoctorAvailable(selectedDoctor.getId(), date, timeSlot)) {
            JOptionPane.showMessageDialog(null, "Doctor is not available at this time.");
            return;
        }

        String apptId = "A" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Appointment a = new Appointment(apptId, p.getId(), p.getName(),
                selectedDoctor.getId(), issue, date, timeSlot, "Confirmed");

        if (apptService.bookAppointment(issue, a)) {
            JOptionPane.showMessageDialog(null, "Appointment booked. ID: " + apptId);
            refreshTable(model);
        } else {
            JOptionPane.showMessageDialog(null, "Booking failed. Try again.");
        }
    }
}
