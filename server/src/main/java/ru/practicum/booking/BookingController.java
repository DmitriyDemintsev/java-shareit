package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingCreateDto;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingMapper;

import javax.validation.Valid;
import java.util.List;

@Component
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        return bookingMapper.toBookingDto(bookingService.create(bookingMapper.toBooking(bookingCreateDto, null),
                userId, bookingCreateDto.getItemId()));
    }

    @PatchMapping("/{id}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable("id") long id,
                                    @RequestParam("approved") boolean approved) {
        return bookingMapper.toBookingDto(bookingService.update(id, approved, userId));
    }

    @GetMapping("/{id}")
    public BookingDto getBookingDtoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long id) {
        return bookingMapper.toBookingDto(bookingService.getBookingById(userId, id));
    }

    @GetMapping
    public List<BookingDto> getAllBookingsForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        return bookingMapper.toBookingDtoList(bookingService.getAllBookings(userId,
                bookingMapper.toStatus(state), from, size));
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsAllItemsForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                       @RequestParam(value = "from", defaultValue = "0") int from,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return bookingMapper.toBookingDtoList(bookingService.getBookingsAllItemsForUser(userId,
                bookingMapper.toStatus(state), from, size));
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") long id) {
        bookingService.deleteBooking(id);
    }
}
