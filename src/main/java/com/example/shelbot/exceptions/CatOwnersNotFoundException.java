package com.example.shelbot.exceptions;

public class CatOwnersNotFoundException extends RuntimeException {
    public CatOwnersNotFoundException() {super("Cat Owner is not found.");}
}
