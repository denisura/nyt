package com.github.denisura.nytseacher.utils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaUtils {

    public static String formatLocalDate(LocalDate date) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMM dd, yyyy");
        return dtfOut.print(date);
    }

    public static String formatAPIDate(LocalDate date) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("YYYYMMdd");
        return dtfOut.print(date);
    }


    public static String formatDate(LocalDate date, String pattern) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(pattern);
        return dtfOut.print(date);
    }
}
