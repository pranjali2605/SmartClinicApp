package com.smartclinic.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to map patient issues to appropriate medical specializations.
 * This class stores predefined mappings between common health issues and their corresponding medical specialties.
 */
public class SpecializationMapper {

    // A static map that stores the relationship between health issues and doctor specializations
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

    /**
     * Given an issue, returns the corresponding specialization for a doctor.

     * This method performs a case-insensitive search to find the matching specialization for a health issue.
     *
     * @param issue The health issue to be mapped to a specialization.
     * @return The specialization corresponding to the issue, or null if no match is found.
     */
    public static String getSpecialization(String issue) {
        if (issue == null) return null;  // Return null if the issue is null
        String lowerCaseIssue = issue.toLowerCase();  // Convert the issue to lowercase to make it case-insensitive
        // Loop through all the keys in the issueToSpecialization map and check if the issue contains any of the keywords
        for (String keyword : issueToSpecialization.keySet()) {
            if (lowerCaseIssue.contains(keyword)) {
                return issueToSpecialization.get(keyword);  // Return the corresponding specialization
            }
        }
        return null;  // Return null if no match is found
    }
}
