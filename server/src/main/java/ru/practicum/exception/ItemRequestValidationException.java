package ru.practicum.exception;

public class ItemRequestValidationException extends RuntimeException {

    public ItemRequestValidationException(final String message) {
        super(message);
    }
}
