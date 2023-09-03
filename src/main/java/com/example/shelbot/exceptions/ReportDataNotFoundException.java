package com.example.shelbot.exceptions;

/**
 * Класс Ошибки отчетов
 * @author Королёв Артем
 **/
public class ReportDataNotFoundException extends RuntimeException {
    public ReportDataNotFoundException(String message) {
        super(message);
    }
}
