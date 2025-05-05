package com.smartclinic.util;

public class InputValidator {
    public static boolean isValidName(String name) {
        return name != null && name.matches("[A-Za-z ]+");
    }

    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 120;
    }

    public static boolean isValidId(String id) {
        return id != null && id.matches("\\d+");
    }
}
