package com.example.shelbot.exceptions;

public class DogOwnersNotFoundException extends RuntimeException {
    public DogOwnersNotFoundException() {
        super("Dog Owner is not found.");
    }
}
