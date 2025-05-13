package com.smartclinic.app;

import com.smartclinic.model.Doctor;
import com.smartclinic.service.DoctorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorMenu {

    // Instance of the DoctorService for managing doctor-related operations
    private static final DoctorService service = new DoctorService();

    /**
     * Shows the Doctor Management menu GUI.
     * This method initializes the main UI components such as table for displaying doctors, search,
     * sort functionality, and buttons for CRUD operations.
     */
    public static void showMenuGUI() {
        JFrame frame = new JFrame("Doctor Management");
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);  // Center the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table setup for displaying doctor details
        String[] columns = {"ID", "Name", "Specialization", "Time Slots"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        refreshDoctorTable(model);  // Populate table with existing doctor data

        // ----- Top Panel: Search & Sort -----
        JPanel topPanel = new JPanel();
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton sortBtn = new JButton("Sort by Name");

        topPanel.add(new JLabel("Search by ID / Name / Specialization:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(sortBtn);

        // Search button action: Filters doctors based on input text
        searchBtn.addActionListener(e -> {
            String input = searchField.getText().trim().toLowerCase();
            if (input.isEmpty()) {
                refreshDoctorTable(model);  // If search is empty, display all doctors
            } else {
                // Filter doctors by ID, name, or specialization
                List<Doctor> results = service.getAllDoctors().stream()
                        .filter(d -> d.getId().toLowerCase().contains(input) ||
                                d.getName().toLowerCase().contains(input) ||
                                d.getSpecialization().toLowerCase().contains(input))
                        .collect(Collectors.toList());

                // Show message if no results match
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No matching doctors found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                }

                // Update table with filtered results
                refreshDoctorTable(model, results);
            }
        });

        // Sort button action: Sort doctors by name
        sortBtn.addActionListener(e -> {
            List<Doctor> sorted = service.getDoctorsSortedByName();
            refreshDoctorTable(model, sorted);
        });

        // ----- Bottom Panel: Action Buttons -----
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Doctor");
        JButton updateBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton viewAllBtn = new JButton("View All");
        JButton backBtn = new JButton("Return to Main Menu");

        // Add action buttons to the panel
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(backBtn);

        // Action for adding a new doctor
        addBtn.addActionListener(e -> showAddDoctorForm(model));

        // Action for updating an existing doctor
        updateBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                String id = (String) model.getValueAt(selected, 0);
                Doctor doc = service.getDoctor(id);
                if (doc != null) {
                    showUpdateDoctorForm(model, doc);  // Show update form for selected doctor
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a doctor to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action for deleting an existing doctor
        deleteBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                String id = (String) model.getValueAt(selected, 0);
                int confirm = JOptionPane.showConfirmDialog(frame, "Delete doctor " + id + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (service.deleteDoctor(id)) {
                        JOptionPane.showMessageDialog(frame, "Doctor deleted.");
                        refreshDoctorTable(model);  // Refresh table after deletion
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a doctor to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action for viewing all doctors
        viewAllBtn.addActionListener(e -> refreshDoctorTable(model));

        // Action for going back to the main menu
        backBtn.addActionListener(e -> {
            frame.dispose();  // Close the current window
            SmartClinicApp.openMainMenu();  // Open main menu
        });

        // Layout setup
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * Refreshes the doctor table with the current list of doctors.
     * @param model the table model
     */
    private static void refreshDoctorTable(DefaultTableModel model) {
        refreshDoctorTable(model, service.getAllDoctors());
    }

    /**
     * Refreshes the doctor table with the specified list of doctors.
     * @param model the table model
     * @param list the list of doctors to display
     */
    private static void refreshDoctorTable(DefaultTableModel model, List<Doctor> list) {
        model.setRowCount(0);  // Clear existing rows
        for (Doctor d : list) {
            model.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialization(), d.getTimeSlots()});
        }
    }

    /**
     * Shows the form to add a new doctor.
     * @param model the table model to refresh after adding the doctor
     */
    private static void showAddDoctorForm(DefaultTableModel model) {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField specField = new JTextField();
        JTextField slotsField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Doctor ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Specialization:")); panel.add(specField);
        panel.add(new JLabel("Time Slots (comma-separated):")); panel.add(slotsField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String spec = specField.getText().trim();
            String slots = slotsField.getText().trim();

            // Validate input
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Doctor ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (service.getDoctor(id) != null) {
                JOptionPane.showMessageDialog(null, "Doctor ID already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!name.matches("[A-Za-z_. ]+")) {
                JOptionPane.showMessageDialog(null, "Invalid name. Only letters and spaces allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!spec.matches("[A-Za-z ]+")) {
                JOptionPane.showMessageDialog(null, "Invalid specialization.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isInvalidTimeSlots(slots)) {
                JOptionPane.showMessageDialog(null, "Invalid time slot format. Use HH:mm separated by commas.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Doctor d = new Doctor(id, name, spec, slots);
            if (service.addDoctor(d)) {
                JOptionPane.showMessageDialog(null, "Doctor added.");
                refreshDoctorTable(model);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add doctor.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Shows the form to update an existing doctor.
     * @param model the table model to refresh after updating the doctor
     * @param existing the existing doctor to be updated
     */
    private static void showUpdateDoctorForm(DefaultTableModel model, Doctor existing) {
        JTextField nameField = new JTextField(existing.getName());
        JTextField specField = new JTextField(existing.getSpecialization());
        JTextField slotsField = new JTextField(existing.getTimeSlots());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Doctor ID (Read-only):")); panel.add(new JLabel(existing.getId()));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Specialization:")); panel.add(specField);
        panel.add(new JLabel("Time Slots (comma-separated):")); panel.add(slotsField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String spec = specField.getText().trim();
            String slots = slotsField.getText().trim();

            // Validate input
            if (!name.matches("[A-Za-z_. ]+")) {
                JOptionPane.showMessageDialog(null, "Invalid name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!spec.matches("[A-Za-z ]+")) {
                JOptionPane.showMessageDialog(null, "Invalid specialization.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isInvalidTimeSlots(slots)) {
                JOptionPane.showMessageDialog(null,  "Invalid time slot format. Use HH:mm format separated by commas.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Doctor updated = new Doctor(existing.getId(), name, spec, slots);
            if (service.updateDoctor(updated)) {
                JOptionPane.showMessageDialog(null, "Doctor updated.");
                refreshDoctorTable(model);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Validates the time slots input to ensure the format is correct.
     * @param input the time slots input string
     * @return true if the format is valid, false otherwise
     */
    private static boolean isInvalidTimeSlots(String input) {
        String[] slots = input.split(",");
        for (String slot : slots) {
            if (!slot.trim().matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                return false;  // found an invalid time slot
            }
        }
        return true;  // all time slots are valid
    }
}
