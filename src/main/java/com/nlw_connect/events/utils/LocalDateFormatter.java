package com.nlw_connect.events.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateFormatter {

    public static String formatDate(LocalDate date) {

        LocalDate dt = LocalDate.parse(date.toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(dt);
    }
}
