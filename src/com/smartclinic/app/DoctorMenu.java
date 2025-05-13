package com.smartclinic.app;

import com.smartclinic.model.Doctor;
import com.smartclinic.service.DoctorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorMenu {
    private static final DoctorService service = new DoctorService();


public static void showMenuGUI() {
    JFrame frame = new JFrame("Doctor Management");
    frame.setSize(800, 450);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    // Table setup
    String[] columns = {"ID", "Name", "Specialization", "Time Slots"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);
    JTable table = new JTable(model);
    refreshDoctorTable(model);

    // ----- Top Panel: Search & Sort -----
    JPanel topPanel = new JPanel();
    JTextField searchField = new JTextField(15);
    JButton searchBtn = new JButton("Search");
    JButton sortBtn = new JButton("Sort by Name");

    topPanel.add(new JLabel("Search by ID / Name / Specialization:"));
    topPanel.add(searchField);
    topPanel.add(searchBtn);
    topPanel.add(sortBtn);

    // Search: by ID, name, or specialization
    searchBtn.addActionListener(e -> {
        String input = searchField.getText().trim().toLowerCase();
        if (input.isEmpty()) {
            refreshDoctorTable(model);
        } else {
            List<Doctor> results = service.getAllDoctors().stream()
                    .filter(d ->
                            d.getId().toLowerCase().contains(input) ||
                                    d.getName().toLowerCase().contains(input) ||
                                    d.getSpecialization().toLowerCase().contains(input))
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No matching doctors found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            }

            refreshDoctorTable(model, results);
        }
    });

    // Sort by name
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

    buttonPanel.add(addBtn);
    buttonPanel.add(updateBtn);
    buttonPanel.add(deleteBtn);
    buttonPanel.add(viewAllBtn);
    buttonPanel.add(backBtn);

    // Action handlers
    addBtn.addActionListener(e -> showAddDoctorForm(model));

    updateBtn.addActionListener(e -> {
        int selected = table.getSelectedRow();
        if (selected != -1) {
            String id = (String) model.getValueAt(selected, 0);
            Doctor doc = service.getDoctor(id);
            if (doc != null) {
                showUpdateDoctorForm(model, doc);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a doctor to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    });

    deleteBtn.addActionListener(e -> {
        int selected = table.getSelectedRow();
        if (selected != -1) {
            String id = (String) model.getValueAt(selected, 0);
            int confirm = JOptionPane.showConfirmDialog(frame, "Delete doctor " + id + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (service.deleteDoctor(id)) {
                    JOptionPane.showMessageDialog(frame, "Doctor deleted.");
                    refreshDoctorTable(model);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a doctor to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    });

    viewAllBtn.addActionListener(e -> refreshDoctorTable(model));

    backBtn.addActionListener(e -> {
        frame.dispose();
        SmartClinicApp.openMainMenu();
    });

    // Layout
    frame.add(topPanel, BorderLayout.NORTH);
    frame.add(new JScrollPane(table), BorderLayout.CENTER);
    frame.add(buttonPanel, BorderLayout.SOUTH);
    frame.setVisible(true);
}

    private static void refreshDoctorTable(DefaultTableModel model) {
        refreshDoctorTable(model, service.getAllDoctors());
    }

    private static void refreshDoctorTable(DefaultTableModel model, List<Doctor> list) {
        model.setRowCount(0);
        for (Doctor d : list) {
            model.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialization(), d.getTimeSlots()});
        }
    }

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

    private static boolean isInvalidTimeSlots(String input) {
        String[] slots = input.split(",");
        for (String slot : slots) {
            if (!slot.trim().matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                return false; // found an invalid one
            }
        }
        return true; // all valid
    }


}
