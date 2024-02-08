package ru.practicum.exception;

public class BookingValidationException extends RuntimeException {
    public BookingValidationException(final String message) {
        super(message);
    }
}
