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
                item.getNextBooking() != null ? BookingMapper.toBookingDto(item.getNextBooking()) : null,
                item.getLastBooking() != null ? BookingMapper.toBookingDto(item.getLastBooking()) : null,
                item.getComments() != null ? CommentMapper.toCommentDtoListList(item.getComments()) : null,
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

//    public static Comment toComment(CommentCreateDto commentCreateDto, Long id) {
//        return new Comment(
//                id,
//                commentCreateDto.getText(),
//                null,
//                null,
//                LocalDateTime.now()
//        );
//    }
//
//    public static CommentDto toCommentDto(Comment comment) {
//        return new CommentDto(
//                comment.getId(),
//                comment.getText(),
//                toItemDto(comment.getItem()),
//                UserMapper.toUserDto(comment.getAuthor()),
//                comment.getAuthor().getName(),
//                comment.getCreated()
//        );
//    }
//
//    public static List<CommentDto> toItemDtoListList(Iterable<Comment> comments) {
//        List<CommentDto> result = new ArrayList<>();
//        for (Comment comment : comments) {
//            result.add(toCommentDto(comment));
//        }
//        return result;
//    }
}
