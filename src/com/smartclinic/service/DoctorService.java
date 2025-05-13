package com.smartclinic.service;

import com.smartclinic.dao.DoctorDAO;
import com.smartclinic.model.Doctor;

import java.util.ArrayList;
import java.util.LinkedList;
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
        return DoctorDAO.getDoctorById(id);
    }

    public List<Doctor> getAllDoctors() {
        return dao.getAllDoctors();
    }

    /**
     * Performs a linear search to find doctors by name.
     *
     * @param name Full or partial doctor name
     * @return List of matching doctors
     */
    public List<Doctor> searchDoctorsByName(String name) {
        LinkedList<Doctor> list = new LinkedList<>(getAllDoctors());
        List<Doctor> matched = new ArrayList<>();
        for (Doctor d : list) {
            if (d.getName().toLowerCase().contains(name.toLowerCase())) {
                matched.add(d);
            }
        }
        return matched;
    }

    /**
     * Returns a list of doctors sorted by name using Merge Sort.
     */
    public List<Doctor> getDoctorsSortedByName() {
        List<Doctor> list = new ArrayList<>(getAllDoctors());
        mergeSortByName(list, 0, list.size() - 1);
        return list;
    }

    private void mergeSortByName(List<Doctor> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortByName(list, left, mid);
            mergeSortByName(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(List<Doctor> list, int left, int mid, int right) {
        List<Doctor> leftList = new ArrayList<>(list.subList(left, mid + 1));
        List<Doctor> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i).getName().compareToIgnoreCase(rightList.get(j).getName()) <= 0) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
        while (i < leftList.size()) list.set(k++, leftList.get(i++));
        while (j < rightList.size()) list.set(k++, rightList.get(j++));
    }

    /**
     * Searches doctors by specialization using linear search.
     */
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        LinkedList<Doctor> list = new LinkedList<>(getAllDoctors());
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : list) {
            if (d.getSpecialization() != null && d.getSpecialization().equalsIgnoreCase(specialization)) {
                result.add(d);
            }
        }
        return result;
    }

}
