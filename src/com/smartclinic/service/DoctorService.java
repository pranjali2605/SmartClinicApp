package com.smartclinic.service;

import com.smartclinic.dao.DoctorDAO;
import com.smartclinic.model.Doctor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Service class that handles operations related to doctors.
 * It interacts with the DoctorDAO to add, update, delete, and retrieve doctor records.
 * It also provides additional functionalities like searching and sorting doctors.
 */
public class DoctorService {
    private final DoctorDAO dao = new DoctorDAO(); // Data Access Object for doctor records

    /**
     * Adds a new doctor to the system.
     * @param d The Doctor object containing doctor details to be added.
     * @return true if the doctor was successfully added, false otherwise.
     */
    public boolean addDoctor(Doctor d) {
        return dao.addDoctor(d);
    }

    /**
     * Updates the information of an existing doctor.
     * @param d The Doctor object containing updated doctor details.
     * @return true if the doctor was successfully updated, false otherwise.
     */
    public boolean updateDoctor(Doctor d) {
        return dao.updateDoctor(d);
    }

    /**
     * Deletes a doctor from the system by their ID.
     * @param id The ID of the doctor to be deleted.
     * @return true if the doctor was successfully deleted, false otherwise.
     */
    public boolean deleteDoctor(String id) {
        return dao.deleteDoctor(id);
    }

    /**
     * Retrieves a doctor by their ID.
     * @param id The ID of the doctor to retrieve.
     * @return The Doctor object corresponding to the given ID, or null if not found.
     */
    public Doctor getDoctor(String id) {
        return DoctorDAO.getDoctorById(id); // Corrected from static method call on DoctorDAO
    }

    /**
     * Retrieves a list of all doctors in the system.
     * @return A list of all Doctor objects.
     */
    public List<Doctor> getAllDoctors() {
        return dao.getAllDoctors();
    }

    /**
     * Performs a linear search to find doctors by name (case-insensitive).
     * @param name The full or partial name of the doctor to search for.
     * @return A list of doctors whose names contain the given search string.
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
     * @return A list of doctors sorted alphabetically by name.
     */
    public List<Doctor> getDoctorsSortedByName() {
        List<Doctor> list = new ArrayList<>(getAllDoctors());
        mergeSortByName(list, 0, list.size() - 1);
        return list;
    }

    /**
     * Helper method to perform merge sort on a list of doctors by name.
     * @param list The list of doctors to be sorted.
     * @param left The left index of the sublist.
     * @param right The right index of the sublist.
     */
    private void mergeSortByName(List<Doctor> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortByName(list, left, mid); // Sort left sublist
            mergeSortByName(list, mid + 1, right); // Sort right sublist
            merge(list, left, mid, right); // Merge the sorted sublists
        }
    }

    /**
     * Helper method to merge two sorted sublists into a single sorted list.
     * @param list The list to merge.
     * @param left The left index of the sublist.
     * @param mid The midpoint index of the sublist.
     * @param right The right index of the sublist.
     */
    private void merge(List<Doctor> list, int left, int mid, int right) {
        List<Doctor> leftList = new ArrayList<>(list.subList(left, mid + 1));
        List<Doctor> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            // Compare names (case-insensitive) and add the smaller one
            if (leftList.get(i).getName().compareToIgnoreCase(rightList.get(j).getName()) <= 0) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
        // Add remaining elements from leftList or rightList
        while (i < leftList.size()) list.set(k++, leftList.get(i++));
        while (j < rightList.size()) list.set(k++, rightList.get(j++));
    }

    /**
     * Searches for doctors by their specialization (case-insensitive).
     * @param specialization The specialization to search for.
     * @return A list of doctors who match the given specialization.
     */
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        LinkedList<Doctor> list = new LinkedList<>(getAllDoctors());
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : list) {
            // Check if the doctor's specialization matches the query
            if (d.getSpecialization() != null && d.getSpecialization().equalsIgnoreCase(specialization)) {
                result.add(d);
            }
        }
        return result;
    }
}
