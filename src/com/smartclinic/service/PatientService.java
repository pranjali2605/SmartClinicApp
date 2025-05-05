package com.smartclinic.service;

import com.smartclinic.dao.PatientDAO;
import com.smartclinic.model.Patient;

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
}
