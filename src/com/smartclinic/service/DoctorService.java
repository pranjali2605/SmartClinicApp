package com.smartclinic.service;

import com.smartclinic.dao.DoctorDAO;
import com.smartclinic.model.Doctor;

import java.util.List;

public class DoctorService {
    private final DoctorDAO dao = new DoctorDAO();

    public boolean addDoctor(Doctor d) {
        return dao.addDoctor(d);
    }

    public boolean updateDoctor(Doctor d) {
        return dao.updateDoctor(d);
    }

    public boolean deleteDoctor(String id) {
        return dao.deleteDoctor(id);
    }

    public Doctor getDoctor(String id) {
        return dao.getDoctorById(id);
    }

    public List<Doctor> getAllDoctors() {
        return dao.getAllDoctors();
    }
}
