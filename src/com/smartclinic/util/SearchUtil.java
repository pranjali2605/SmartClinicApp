package com.smartclinic.util;

import com.smartclinic.model.Appointment;

import java.util.List;

public class SearchUtil {
    public static void searchByPatient(List<Appointment> list, String name) {
        for (Appointment a : list) {
            if (a.getPatientName().equalsIgnoreCase(name)) {
                System.out.println(a);
            }
        }
    }
}
