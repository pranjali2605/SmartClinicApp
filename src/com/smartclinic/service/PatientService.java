package com.smartclinic.service;

import com.smartclinic.dao.PatientDAO;
import com.smartclinic.model.Patient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Service class that manages operations related to patients.
 * It interacts with the PatientDAO to add, update, delete, and retrieve patient records.
 * It also provides functionalities for sorting and searching patients based on certain fields.
 */
public class PatientService {
    private final PatientDAO dao = new PatientDAO(); // Data Access Object for patient records

    /**
     * Adds a new patient to the system.
     * @param p The Patient object containing patient details to be added.
     * @return true if the patient was successfully added, false otherwise.
     */
    public boolean addPatient(Patient p) {
        return dao.addPatient(p);
    }

    /**
     * Updates the information of an existing patient.
     * @param p The Patient object containing updated patient details.
     * @return true if the patient was successfully updated, false otherwise.
     */
    public boolean updatePatient(Patient p) {
        return dao.updatePatient(p);
    }

    /**
     * Deletes a patient from the system by their ID.
     * @param id The ID of the patient to be deleted.
     * @return true if the patient was successfully deleted, false otherwise.
     */
    public boolean deletePatient(String id) {
        return dao.deletePatient(id);
    }

    /**
     * Retrieves a patient by their ID.
     * @param id The ID of the patient to retrieve.
     * @return The Patient object corresponding to the given ID, or null if not found.
     */
    public Patient getPatient(String id) {
        return dao.getPatientById(id);
    }

    /**
     * Retrieves a list of all patients in the system.
     * @return A list of all Patient objects.
     */
    public List<Patient> getAllPatients() {
        return dao.getAllPatients();
    }

    /**
     * Returns a LinkedList of patients sorted by name using Bubble Sort.
     * Bubble Sort is not the most efficient sorting algorithm, but it is simple to implement.
     * @return A LinkedList of patients sorted alphabetically by name.
     */
    public List<Patient> getPatientsSortedByName() {
        LinkedList<Patient> patientsList = new LinkedList<>(getAllPatients());
        int n = patientsList.size();
        // Bubble sort algorithm for sorting patients by name
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Patient p1 = patientsList.get(j);
                Patient p2 = patientsList.get(j + 1);
                if (p1.getName().compareToIgnoreCase(p2.getName()) > 0) {
                    // Swap patients if they are in the wrong order
                    patientsList.set(j, p2);
                    patientsList.set(j + 1, p1);
                }
            }
        }
        return patientsList;
    }

    /**
     * Searches for patients by their medical issue using linear search.
     * @param issue The issue to search for.
     * @return A list of patients who match the given issue.
     */
    public List<Patient> searchPatientsByIssue(String issue) {
        LinkedList<Patient> patients = new LinkedList<>(getAllPatients());
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            // Case-insensitive comparison of patient issue
            if (p.getIssue() != null && p.getIssue().equalsIgnoreCase(issue)) {
                result.add(p);
            }
        }
        return result;
    }
}
