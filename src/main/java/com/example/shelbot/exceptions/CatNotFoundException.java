package com.example.shelbot.exceptions;

/**
 * Класс Ошибки отчетов
 * @author Герасименко Максим
 */
public class CatNotFoundException extends RuntimeException{
    public CatNotFoundException() {
        super("Cat is not found.");
    }
}
