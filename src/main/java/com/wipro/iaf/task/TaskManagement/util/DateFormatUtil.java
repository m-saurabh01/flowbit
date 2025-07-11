package com.wipro.iaf.task.TaskManagement.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    public static String formatDate(LocalDate date, String pattern) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
