package com.crm.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Shared date-time formatting utility for the CRM system.
 *
 * Centralises the display format so all classes print timestamps
 * consistently without duplicating formatter definitions.
 *
 * Output example: 2026-03-17 7:51 PM
 */
public class DateTimeUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

    // Utility class — no instantiation needed
    private DateTimeUtil() {}

    /**
     * Formats a LocalDateTime into the CRM standard display format.
     * Example: 2026-03-17 7:51 PM
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(FORMATTER);
    }

    /**
     * Formats the current moment into the CRM standard display format.
     */
    public static String now() {
        return format(LocalDateTime.now());
    }
}
