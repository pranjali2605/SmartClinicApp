package com.smartclinic.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class that provides methods related to date formatting.
 * It provides functionality to format `Date` objects into a specific string format.
 */
public class DateUtils {

    /**
     * Formats a given Date object into a string in the format "yyyy-MM-dd HH:mm".
     *
     * @param date The Date object to be formatted.
     * @return A string representation of the date in the format "yyyy-MM-dd HH:mm".
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);  // Returns the formatted date string
    }
}
