package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    public final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemDto requestDto,
                                             @PathVariable("id") long id) {
        return itemClient.updateItem(userId, requestDto, id);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object>  deleteItem(@PathVariable("itemId") long itemId) {
        return itemClient.deleteItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                           @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get booking userId={}, from={}, size={}", userId, from, size);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long id) {
        log.info("Get item {}, userId={}", id, userId);
        return itemClient.getItemById(userId, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchItem(@RequestParam("text") String query,
                                                @PositiveOrZero @RequestParam(value = "from",
                                                        defaultValue = "0") int from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemClient.getSearchItem(query, from, size);
    }
}
