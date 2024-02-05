package ru.practicum.exception;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(final String message) {
        super(message);
    }
}
