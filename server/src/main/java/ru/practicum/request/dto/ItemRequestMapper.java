package ru.practicum.request.dto;

import ru.practicum.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, Long id) {
        return new ItemRequest(
                id,
                itemRequestDto.getDescription(),
                null,
                LocalDateTime.now()
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                null
        );
    }

    public static List<ItemRequestDto> toItemRequestDtoList(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            result.add(toItemRequestDto(itemRequest));
        }
        return result;
    }
}
