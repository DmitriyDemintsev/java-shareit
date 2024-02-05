package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.dto.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.dto.CommentMapper;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.model.Comment;

import javax.validation.Valid;
import java.util.List;

@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        List<ItemDto> itemDtos = itemMapper.toItemDtoList(itemService.getItems(userId, from, size));
        for (ItemDto itemDto : itemDtos) {
            addComment(itemDto);
            if (itemDto.getOwner().equals(userId)) {
                addBookings(itemDto);
            }
        }
        return itemDtos;
    }

    @GetMapping("/{id}")
    public ItemDto getItemDtoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long id) {
        ItemDto itemDto = itemMapper.toItemDto(itemService.getItemById(id));
        addComment(itemDto);
        if (itemDto.getOwner().equals(userId)) {
            addBookings(itemDto);
        }
        return itemDto;
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItem(@RequestParam("text") String query,
                                       @RequestParam(value = "from", defaultValue = "0") int from,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemMapper.toItemDtoList(itemService.getItemsBySearch(query, from, size));
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        return itemMapper.toItemDto(itemService.create(userId, itemDto.getRequestId(),
                itemMapper.toItem(itemDto, null)));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto,
                              @PathVariable("id") long id) {
        return itemMapper.toItemDto(itemService.update(userId, itemMapper.toItem(itemDto, id)));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") long itemId) {
        itemService.deleteItem(itemId);
    }

    private void addComment(ItemDto itemDto) {
        List<Comment> comments = commentService.getComments(itemDto.getId());
        itemDto.setComments(commentMapper.toItemDtoListList(comments));
    }

    private void addBookings(ItemDto itemDto) {
        Booking last = bookingService.getLast(itemDto.getId());
        Booking next = bookingService.getNext(itemDto.getId());
        if (last != null) {
            itemDto.setLastBooking(bookingMapper.toBookingDto(last));
        }
        if (next != null) {
            itemDto.setNextBooking(bookingMapper.toBookingDto(next));
        }
    }
}
