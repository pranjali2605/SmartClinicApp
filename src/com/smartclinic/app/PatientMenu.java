package com.smartclinic.app;

import com.smartclinic.model.Patient;
import com.smartclinic.service.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PatientMenu {
    private static final PatientService service = new PatientService();


    public static void showMenuGUI() {
        JFrame frame = new JFrame("Patient Management");
        frame.setSize(900, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Name", "Age", "Contact", "Gender", "Issue"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        refreshPatientTable(model, service.getAllPatients());

        // ----- Top Panel: Search -----
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        searchPanel.add(new JLabel("Search by Name / Id / Issue:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Search logic
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            if (query.isEmpty()) {
                refreshPatientTable(model, service.getAllPatients());
            } else {
                List<Patient> results = service.getAllPatients().stream()
                        .filter(p -> p.getName().toLowerCase().contains(query)
                                || p.getId().toLowerCase().contains(query)
                                || p.getIssue().toLowerCase().contains(query))
                        .collect(Collectors.toList());
                refreshPatientTable(model, results);
            }
        });

        // ----- Bottom Panel: CRUD Buttons -----
        JPanel buttonPanel = createPatientMenuPanel(frame, model, table);

        // Add to frame
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }


    // Method to create the patient menu panel with action buttons
    private static JPanel createPatientMenuPanel(JFrame frame, DefaultTableModel model, JTable table) {
        JPanel panel = new JPanel();

        JButton addBtn = new JButton("Add Patient");
        JButton updateBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton sortBtn = new JButton("Sort by Name");
        JButton searchBtn = new JButton("Search by Issue");
        JButton viewAllBtn = new JButton("View All Patients");
        JButton backBtn = new JButton("Return to Main Menu");

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(sortBtn);
        panel.add(searchBtn);
        panel.add(viewAllBtn);
        panel.add(backBtn);

        addBtn.addActionListener(e -> showAddPatientForm(model));
        updateBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                String id = (String) model.getValueAt(selected, 0);
                Patient patient = service.getPatient(id);
                showUpdatePatientForm(model, patient);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a patient to update.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                String id = (String) model.getValueAt(selected, 0);
                int confirm = JOptionPane.showConfirmDialog(frame, "Delete patient " + id + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    if (service.deletePatient(id)) {
                        JOptionPane.showMessageDialog(frame, "Patient deleted.");
                        refreshPatientTable(model, service.getAllPatients());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Deletion failed.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a patient to delete.");
            }
        });

        sortBtn.addActionListener(e -> {
            List<Patient> sorted = service.getPatientsSortedByName();
            refreshPatientTable(model, sorted);
        });

        searchBtn.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(frame, "Enter keyword to search by issue:");
            if (keyword != null && !keyword.trim().isEmpty()) {
                String lowerKeyword = keyword.trim().toLowerCase();

                List<Patient> allPatients = service.getAllPatients();
                List<Patient> filtered = allPatients.stream()
                        .filter(p -> p.getIssue() != null && p.getIssue().toLowerCase().contains(lowerKeyword))
                        .toList();

                if (filtered.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No patients found for keyword: " + keyword);
                } else {
                    refreshPatientTable(model, filtered);
                }
            }
        });


        viewAllBtn.addActionListener(e -> refreshPatientTable(model, service.getAllPatients()));

        backBtn.addActionListener(e -> {
            frame.dispose();
            SmartClinicApp.openMainMenu();
        });

        return panel;
    }

    private static void refreshPatientTable(DefaultTableModel model, List<Patient> list) {
        model.setRowCount(0);
        for (Patient p : list) {
            model.addRow(new Object[]{
                    p.getId(), p.getName(), p.getAge(), p.getContact(), p.getGender(), p.getIssue()
            });
        }
    }

    private static void showAddPatientForm(DefaultTableModel model) {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField contactField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField issueField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Patient ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Contact (10 digits):"));
        panel.add(contactField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderBox);
        panel.add(new JLabel("Health Issue:"));
        panel.add(issueField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String contact = contactField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String issue = issueField.getText().trim();

            if (!name.matches("[A-Za-z ]+")) {
                JOptionPane.showMessageDialog(null, "Name must only contain letters and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE); return;
            }
            if (!contact.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null, "Contact number must be exactly 10 digits.", "Input Error", JOptionPane.ERROR_MESSAGE); return;
            }
            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age <= 0 || age > 120) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid age between 1 and 120.", "Input Error", JOptionPane.ERROR_MESSAGE); return;
            }
            if (service.getPatient(id) != null) {
                JOptionPane.showMessageDialog(null, "Patient ID already exists. Please use a different ID.", "Duplicate ID", JOptionPane.WARNING_MESSAGE); return;
            }

            Patient newPatient = new Patient(id, name, age, contact, gender, issue);
            if (service.addPatient(newPatient)) {
                JOptionPane.showMessageDialog(null, "Patient added.");
                refreshPatientTable(model, service.getAllPatients());
            } else {
                JOptionPane.showMessageDialog(null, "Could not add patient. Please check the details or try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void showUpdatePatientForm(DefaultTableModel model, Patient existing) {
        JTextField nameField = new JTextField(existing.getName());
        JTextField ageField = new JTextField(String.valueOf(existing.getAge()));
        JTextField contactField = new JTextField(existing.getContact());
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setSelectedItem(existing.getGender());
        JTextField issueField = new JTextField(existing.getIssue());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Patient ID (Read-only):"));
        panel.add(new JLabel(existing.getId()));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Contact (10 digits):"));
        panel.add(contactField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderBox);
        panel.add(new JLabel("Health Issue:"));
        panel.add(issueField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String contact = contactField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String issue = issueField.getText().trim();

            if (!name.matches("[A-Za-z ]+")) {
                JOptionPane.showMessageDialog(null, "Invalid name."); return;
            }
            if (!contact.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null, "Invalid contact number."); return;
            }
            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age <= 0 || age > 120) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid age."); return;
            }

            existing.setName(name);
            existing.setAge(age);
            existing.setContact(contact);
            existing.setGender(gender);
            existing.setIssue(issue);

            if (service.updatePatient(existing)) {
                JOptionPane.showMessageDialog(null, "Patient updated.");
                refreshPatientTable(model, service.getAllPatients());
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. Please check the inputs and try again.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
