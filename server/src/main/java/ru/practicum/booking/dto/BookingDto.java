package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto implements Serializable {
    private Long id;
    private LocalDateTime start; //дата и время начала бронирование
    private LocalDateTime end; //дата и время окончания бронирования
    private ItemDto item; //что бронируют
    private UserDto booker; //кто бронирует
    private Long bookerId;
    private BookingStatus status; //статус бронирования
}
