package ru.practicum.exception;

public class ItemValidationException extends RuntimeException{

    public ItemValidationException(final String message) {
        super(message);
    }
}
