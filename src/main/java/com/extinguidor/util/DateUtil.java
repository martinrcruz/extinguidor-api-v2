package com.extinguidor.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
    
    private DateUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Normaliza una fecha al mediodía (12:00) para evitar problemas de timezone
     */
    public static LocalDate normalizeToNoon(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date;
    }
    
    /**
     * Normaliza una fecha al día 1 del mes a las 12:00
     */
    public static LocalDate normalizeToFirstOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(1);
    }
    
    /**
     * Convierte un string YYYY-MM-DD a LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString.trim(), DATE_FORMATTER);
    }
    
    /**
     * Formatea una LocalDate a string YYYY-MM-DD
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Obtiene el primer día del mes actual
     */
    public static LocalDate getFirstDayOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }
    
    /**
     * Obtiene el último día del mes especificado
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(date.lengthOfMonth());
    }
    
    /**
     * Verifica si una fecha es anterior al mes actual
     */
    public static boolean isBeforeCurrentMonth(LocalDate date) {
        if (date == null) {
            return false;
        }
        LocalDate firstDayOfCurrentMonth = getFirstDayOfCurrentMonth();
        return date.isBefore(firstDayOfCurrentMonth);
    }
    
    /**
     * Crea un LocalDateTime al mediodía de una fecha
     */
    public static LocalDateTime toNoon(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(12, 0);
    }
    
    /**
     * Convierte un timestamp (Long) a LocalDateTime
     */
    public static LocalDateTime fromTimestamp(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, 
            ZonedDateTime.now(DEFAULT_ZONE).getOffset());
    }
    
    /**
     * Convierte un LocalDateTime a timestamp (Long)
     */
    public static Long toTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }
}

