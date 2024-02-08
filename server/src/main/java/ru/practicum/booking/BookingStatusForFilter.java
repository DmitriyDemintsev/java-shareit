package ru.practicum.booking;

public enum BookingStatusForFilter {
    ALL, //статус по умолчанию для запроса Filter
    FUTURE, //будущие
    PAST, //завершённые
    CURRENT, //текущие
    WAITING, //ожидающие подтверждения
    REJECTED //отклонённые
}
