package com.smartclinic.util;

import java.util.HashMap;
import java.util.Map;

public class SpecializationMapper {
    private static final Map<String, String> issueToSpecialization = new HashMap<>();

    static {
        // Cardiology
        issueToSpecialization.put("heart",        "Cardiologist");
        issueToSpecialization.put("cardiac",      "Cardiologist");
        issueToSpecialization.put("chest pain",   "Cardiologist");
        issueToSpecialization.put("angina",       "Cardiologist");

        // Dermatology
        issueToSpecialization.put("skin",         "Dermatologist");
        issueToSpecialization.put("rash",         "Dermatologist");
        issueToSpecialization.put("acne",         "Dermatologist");
        issueToSpecialization.put("eczema",       "Dermatologist");
        issueToSpecialization.put("psoriasis",    "Dermatologist");

        // Ophthalmology
        issueToSpecialization.put("eye",          "Ophthalmologist");
        issueToSpecialization.put("eyes",         "Ophthalmologist");
        issueToSpecialization.put("vision",       "Ophthalmologist");
        issueToSpecialization.put("blurry",       "Ophthalmologist");
        issueToSpecialization.put("optic",        "Ophthalmologist");

        // Orthopedics
        issueToSpecialization.put("bone",         "Orthopedic");
        issueToSpecialization.put("joint",        "Orthopedic");
        issueToSpecialization.put("fracture",     "Orthopedic");
        issueToSpecialization.put("sprain",       "Orthopedic");

        // Dentistry
        issueToSpecialization.put("tooth",        "Dentist");
        issueToSpecialization.put("teeth",        "Dentist");
        issueToSpecialization.put("gum",          "Dentist");
        issueToSpecialization.put("molars",       "Dentist");

        // Pulmonology
        issueToSpecialization.put("lung",         "Pulmonologist");
        issueToSpecialization.put("lungs",        "Pulmonologist");
        issueToSpecialization.put("cough",        "Pulmonologist");
        issueToSpecialization.put("asthma",       "Pulmonologist");
        issueToSpecialization.put("breath",       "Pulmonologist");

        // Neurology
        issueToSpecialization.put("headache",     "Neurologist");
        issueToSpecialization.put("migraine",     "Neurologist");
        issueToSpecialization.put("seizure",      "Neurologist");

        // Gastroenterology
        issueToSpecialization.put("stomach",      "Gastroenterologist");
        issueToSpecialization.put("abdomen",      "Gastroenterologist");
        issueToSpecialization.put("liver",        "Hepatologist"); // or Gastroenterologist

        // Urology / Nephrology
        issueToSpecialization.put("kidney",       "Nephrologist");
        issueToSpecialization.put("urine",        "Urologist");
        issueToSpecialization.put("bladder",      "Urologist");

        // ENT
        issueToSpecialization.put("ear",          "ENT");
        issueToSpecialization.put("nose",         "ENT");
        issueToSpecialization.put("throat",       "ENT");
        issueToSpecialization.put("sinus",        "ENT");

        // Pediatrics / Geriatrics
        issueToSpecialization.put("child",        "Pediatrician");
        issueToSpecialization.put("kid",          "Pediatrician");
        issueToSpecialization.put("pregnancy",    "Gynecologist");
        issueToSpecialization.put("women",        "Gynecologist");

        // Psychiatry
        issueToSpecialization.put("depression",   "Psychiatrist");
        issueToSpecialization.put("anxiety",      "Psychiatrist");
        issueToSpecialization.put("stress",       "Psychiatrist");

        // Endocrinology
        issueToSpecialization.put("diabetes",     "Endocrinologist");
        issueToSpecialization.put("hormone",      "Endocrinologist");

        // Infectious Disease
        issueToSpecialization.put("infection",    "Infectious Disease Specialist");
        issueToSpecialization.put("fever",        "General Physician");
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
