package com.shorten.api.system;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utilities class for shared functionality
 */
public class Utilities {

    /**
     * Return LocalDate in "yyyy-MM-dd" format
     *
     * @param date
     * @return LocalDate in "yyyy-MM-dd" format
     */
    public static LocalDate getDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        return LocalDate.parse(date, formatter);
    }

}
