package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookItemRequestDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.exception.ErrorResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") long userId, // владелец вещи
                                                @PathVariable("id") long id, // бронь
                                                @RequestParam("approved") boolean approved) {
        return bookingClient.updateBooking(userId, approved, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>  deleteBooking(@PathVariable("id") long id) {
        return bookingClient.deleteBooking(id);
    }

    /* бронирования конкретного юзера вклдчая статус */
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(value = "state",
                                                      defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(value = "from",
                                                      defaultValue = "0") Integer from,
                                              @Positive @RequestParam(value = "size",
                                                      defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElse(null);
        if (state == null) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Unknown state: " + stateParam)
            );
        }
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    /* конкретное бронирование, включая статус */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    /* лист бронирования всех item текущего пользователя */
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsAllItemsForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @RequestParam(value = "state",
                                                                     defaultValue = "ALL") String stateParam,
                                                             @PositiveOrZero @RequestParam(value = "from",
                                                                     defaultValue = "0") Integer from,
                                                             @Positive @RequestParam(value = "size",
                                                                     defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElse(null);
        if (state == null) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Unknown state: " + stateParam)
            );
        }
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsForUser(userId, state, from, size);
    }
}
