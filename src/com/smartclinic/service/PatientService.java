package com.smartclinic.service;

import com.smartclinic.dao.PatientDAO;
import com.smartclinic.model.Patient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PatientService {
    private final PatientDAO dao = new PatientDAO();

    public boolean addPatient(Patient p) {
        return dao.addPatient(p);
    }

    public boolean updatePatient(Patient p) {
        return dao.updatePatient(p);
    }

    public boolean deletePatient(String id) {
        return dao.deletePatient(id);
    }

    public Patient getPatient(String id) {
        return dao.getPatientById(id);
    }

    public List<Patient> getAllPatients() {
        return dao.getAllPatients();
    }

    /**
     * Returns a LinkedList of patients sorted by name using bubble sort.
     */
    public List<Patient> getPatientsSortedByName() {
        LinkedList<Patient> patientsList = new LinkedList<>(getAllPatients());
        int n = patientsList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Patient p1 = patientsList.get(j);
                Patient p2 = patientsList.get(j + 1);
                if (p1.getName().compareToIgnoreCase(p2.getName()) > 0) {
                    // Swap
                    patientsList.set(j, p2);
                    patientsList.set(j + 1, p1);
                }
            }
        }
        return patientsList;
    }


    /**
     * Searches patients by issue using linear search.
     */
    public List<Patient> searchPatientsByIssue(String issue) {
        LinkedList<Patient> patients = new LinkedList<>(getAllPatients());
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getIssue() != null && p.getIssue().equalsIgnoreCase(issue)) {
                result.add(p);
            }
        }
        return result;
    }


}
