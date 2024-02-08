package ru.practicum.item.dto;

import ru.practicum.booking.dto.BookingMapper;
import ru.practicum.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static Item toItem(ItemDto itemDto, Long id) {
        Item item = new Item(
                id,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null,
                null,
                null,
                null
        );
        return item;
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                item.getNextBooking() != null ? BookingMapper.toBookingDto(item.getNextBooking(), false) : null,
                item.getLastBooking() != null ? BookingMapper.toBookingDto(item.getLastBooking(), false) : null,
                item.getComments() != null ? CommentMapper.toCommentDtoListList(item.getComments(), false) : null,
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static List<ItemDto> toItemDtoList(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(toItemDto(item));
        }
        return result;
    }
}
