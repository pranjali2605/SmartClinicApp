package com.smartclinic.util;

import com.smartclinic.model.Appointment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtil {
    public static void sortByDate(List<Appointment> list) {
        Collections.sort(list, Comparator.comparing(Appointment::getDateTime));
    }
}
