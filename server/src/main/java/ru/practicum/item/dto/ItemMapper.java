package ru.practicum.item.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class ItemMapper {

    public Item toItem(ItemDto itemDto, Long id) {
        Item item = new Item(
                id,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null
        );
        return item;
    }

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                null,
                null,
                null,
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public List<ItemDto> toItemDtoList(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(toItemDto(item));
        }
        return result;
    }
}
