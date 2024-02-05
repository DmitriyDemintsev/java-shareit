package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.booking.dto.BookItemRequestDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /* PostMapping */
    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    /* PatchMapping("/{id}") */
    public ResponseEntity<Object> updateBooking(long userId, boolean approved, long id) {
        Map<String, Object> parameters = Map.of(
                "id", id,
                "approved", approved
        );
        return patch("/{id}?approved={approved}", userId, parameters, null);
    }

    /* DeleteMapping("/{id}") */
    public ResponseEntity<Object> deleteBooking(long id) {
        Map<String, Object> parameters = Map.of("id", id);
        return delete("/{id}", id, parameters);
    }

    /* GetMapping - бронирования конкретного юзера вклдчая статус */
    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    /* GetMapping("/{id}") - конкретное бронирование, включая статус */
    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        Map<String, Object> parameters = Map.of("id", bookingId);
        return get("/{id}", userId, parameters);
    }

    /* GetMapping("/owner") - лист бронирования всех item текущего пользователя */
    public ResponseEntity<Object> getBookingsForUser(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}
