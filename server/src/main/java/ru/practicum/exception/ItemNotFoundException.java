package ru.practicum.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(final String message) {
        super(message);
    }
}
