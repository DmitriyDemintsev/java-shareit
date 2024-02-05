package ru.practicum.request.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class ItemRequestMapper {

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, Long id) {
        return new ItemRequest(
                id,
                itemRequestDto.getDescription(),
                null,
                LocalDateTime.now()
        );
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                null
        );
    }

    public List<ItemRequestDto> toItemRequestDtoList(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            result.add(toItemRequestDto(itemRequest));
        }
        return result;
    }

    public List<ItemRequest> toItemRequestList(Iterable<ItemRequestDto> itemRequestDtos) {
        List<ItemRequest> result = new ArrayList<>();
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            result.add(toItemRequest(itemRequestDto, itemRequestDto.getId()));
        }
        return result;
    }
}
