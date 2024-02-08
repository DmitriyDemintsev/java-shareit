package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.booking.BookingStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreateDto implements Serializable {
    private Long id;
    private LocalDateTime start; //дата и время начала бронирование
    private LocalDateTime end; //дата и время окончания бронирования
    private Long itemId; //что бронируют
    private BookingStatus status; //статус бронирования
}
