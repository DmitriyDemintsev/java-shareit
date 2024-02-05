package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.booking.BookingStatusForFilter;
import ru.practicum.booking.model.Booking;
import ru.practicum.exception.BookingValidationException;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Booking toBooking(BookingCreateDto bookingCreateDto, Long id) {
        Booking booking = new Booking(
                id,
                bookingCreateDto.getStart(),
                bookingCreateDto.getEnd(),
                null,
                null,
                bookingCreateDto.getStatus()
        );
        return booking;
    }

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemMapper.toItemDto(booking.getItem()),
                userMapper.toUserDto(booking.getBooker()),
                booking.getBooker().getId(),
                booking.getStatus()
        );
        return bookingDto;
    }

    public BookingStatusForFilter toStatus(String status) {
        try {
            return BookingStatusForFilter.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BookingValidationException("Unknown state: " + status);
        }
    }

    public List<BookingDto> toBookingDtoList(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingDto(booking));
        }
        return result;
    }
}
