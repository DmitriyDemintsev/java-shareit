package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> itemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Creating request {}, userId={}", requestDto, userId);
        return requestClient.itemRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getListMineRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get itemRequest userId={}", userId);
        return requestClient.getMineRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get allRequest userId={}, from={}, size={}", userId, from, size);
        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable("requestId") long requestId) {
        log.info("Get itemRequest userId={}, requestId={}", userId, requestId);
        return requestClient.getItemRequestById(userId, requestId);
    }
}
