package ru.practicum.booking;

public enum BookingStatus {
    WAITING, //новое бронирование, ждет подтверждения
    APPROVED, //бронирование подтверждено владельцем
    REJECTED, //бронирование отклонено владельцем
    CANCELED //бронирование отменено создателем
}
