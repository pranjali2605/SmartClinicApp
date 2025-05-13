package com.smartclinic.util;

/**
 * Utility class that provides methods for input validation.
 * This class offers basic validation methods for name, age, and ID inputs.
 */
public class InputValidator {

    /**
     * Validates if a given name is a valid name (only contains letters and spaces).
     *
     * @param name The name to be validated.
     * @return true if the name is valid (only letters and spaces), false otherwise.
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("[A-Za-z ]+");
    }

    /**
     * Validates if the provided age is within the acceptable range (0-120).
     *
     * @param age The age to be validated.
     * @return true if the age is between 0 and 120 (inclusive), false otherwise.
     */
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 120;
    }

    /**
     * Validates if a given ID is a valid numeric ID.
     *
     * @param id The ID to be validated.
     * @return true if the ID consists of only digits, false otherwise.
     */
    public static boolean isValidId(String id) {
        return id != null && id.matches("\\d+");
    }
}
