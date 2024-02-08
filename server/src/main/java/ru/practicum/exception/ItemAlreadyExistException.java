package ru.practicum.exception;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException(final String message) {
        super(message);
    }
}
