package com.smartclinic.util;

import java.util.HashMap;
import java.util.Map;

public class SpecializationMapper {
    private static final Map<String, String> issueToSpecialization = new HashMap<>();

    static {
        issueToSpecialization.put("heart", "Cardiologist");
        issueToSpecialization.put("skin", "Dermatologist");
        issueToSpecialization.put("eyes", "Ophthalmologist");
        issueToSpecialization.put("bones", "Orthopedic");
        issueToSpecialization.put("teeth", "Dentist");
        issueToSpecialization.put("lungs", "Pulmonologist");
        // Add more mappings as needed
    }

    public static String getSpecialization(String issue) {
        if (issue == null) return null;
        String lowerCaseIssue = issue.toLowerCase();
        for (String keyword : issueToSpecialization.keySet()) {
            if (lowerCaseIssue.contains(keyword)) {
                return issueToSpecialization.get(keyword);
            }
        }
        return null; // If no match found
    }
}
