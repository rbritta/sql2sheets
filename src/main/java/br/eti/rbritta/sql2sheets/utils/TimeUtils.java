package br.eti.rbritta.sql2sheets.utils;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

public class TimeUtils {

    public static final String DDMMYYYY_HHMMSS = "dd/MM/yyyy HH:mm:ss";

    public static String format(LocalDateTime timestamp, String pattern) {
        return timestamp.format(ofPattern(pattern));
    }
}
