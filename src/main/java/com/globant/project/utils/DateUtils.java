package com.globant.project.utils;

import java.time.LocalDateTime;

/**
 * DateUtils
 */
public class DateUtils {

    public static LocalDateTime getDateFromString(String date) {
        Integer startDateYear = Integer.parseInt(date.substring(0, 4));
        Integer startDateMonth = Integer.parseInt(date.substring(5, 6));
        Integer startDateDay = Integer.parseInt(date.substring(7, 8));

        return LocalDateTime.of(startDateYear, startDateMonth, startDateDay, 0, 0, 0);

    }
}