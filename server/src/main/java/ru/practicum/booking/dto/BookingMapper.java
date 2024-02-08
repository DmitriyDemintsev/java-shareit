package ru.practicum.booking.dto;

import ru.practicum.booking.BookingStatusForFilter;
import ru.practicum.booking.model.Booking;
import ru.practicum.exception.BookingValidationException;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    public static Booking toBooking(BookingCreateDto bookingCreateDto, Long id) {
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

    public static BookingDto toBookingDto(Booking booking, boolean isIncludeItem) {
        if (isIncludeItem == true) {
            return new BookingDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    ItemMapper.toItemDto(booking.getItem()),
                    UserMapper.toUserDto(booking.getBooker()),
                    booking.getBooker().getId(),
                    booking.getStatus()
            );
        }
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                null,
                UserMapper.toUserDto(booking.getBooker()),
                booking.getBooker().getId(),
                booking.getStatus()
        );

    }

    public static BookingStatusForFilter toStatus(String status) {
        try {
            return BookingStatusForFilter.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BookingValidationException("Unknown state: " + status);
        }
    }

    public static List<BookingDto> toBookingDtoList(Iterable<Booking> bookings, boolean isIncludeItem) {
        List<BookingDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingDto(booking, isIncludeItem));
        }
        return result;
    }
}
