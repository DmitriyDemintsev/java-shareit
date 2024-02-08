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

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        return BookingMapper.toBookingDto(bookingService.create(BookingMapper.toBooking(bookingCreateDto, null),
                userId, bookingCreateDto.getItemId()), true);
    }

    @PatchMapping("/{id}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable("id") long id,
                                    @RequestParam("approved") boolean approved) {
        return BookingMapper.toBookingDto(bookingService.update(id, approved, userId), true);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingDtoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long id) {
        return BookingMapper.toBookingDto(bookingService.getBookingById(userId, id), true);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        return BookingMapper.toBookingDtoList(bookingService.getAllBookings(userId,
                BookingMapper.toStatus(state), from, size), true);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsAllItemsForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                       @RequestParam(value = "from", defaultValue = "0") int from,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return BookingMapper.toBookingDtoList(bookingService.getBookingsAllItemsForUser(userId,
                BookingMapper.toStatus(state), from, size), true);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") long id) {
        bookingService.deleteBooking(id);
    }
}
