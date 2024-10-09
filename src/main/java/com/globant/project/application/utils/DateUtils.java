package com.globant.project.application.utils;

import java.time.LocalDateTime;

import lombok.experimental.UtilityClass;

/**
 * DateUtils
 */
@UtilityClass
public class DateUtils {

    public LocalDateTime getDateFromString(String date) {
        Integer startDateYear = Integer.parseInt(date.substring(0, 4));
        Integer startDateMonth = Integer.parseInt(date.substring(5, 6));
        Integer startDateDay = Integer.parseInt(date.substring(7, 8));

        return LocalDateTime.of(startDateYear, startDateMonth, startDateDay, 0, 0, 0);

    }
}
