package ru.practicum.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.model.Booking;

import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional
    Booking create(Booking booking, Long userId, Long itemId);

    @Transactional
    Booking update(long id, boolean approved, long userId);

    @Transactional
    void deleteBooking(long id);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getAllBookings(long userId, BookingStatusForFilter state, int from, int size);

    List<Booking> getBookingsAllItemsForUser(long userId, BookingStatusForFilter status, int from, int size);

    Booking getLast(Long itemId);

    Booking getNext(Long itemId);
}
