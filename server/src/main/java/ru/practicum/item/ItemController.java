package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;

import javax.validation.Valid;
import java.util.List;

@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        return ItemMapper.toItemDtoList(itemService.getItems(userId, from, size));
    }

    @GetMapping("/{id}")
    public ItemDto getItemDtoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long id) {
        return ItemMapper.toItemDto(itemService.getItemById(userId, id));
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItem(@RequestParam("text") String query,
                                       @RequestParam(value = "from", defaultValue = "0") int from,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return ItemMapper.toItemDtoList(itemService.getItemsBySearch(query, from, size));
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.create(userId, itemDto.getRequestId(),
                ItemMapper.toItem(itemDto, null)));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto,
                              @PathVariable("id") long id) {
        return ItemMapper.toItemDto(itemService.update(userId, ItemMapper.toItem(itemDto, id)));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") long itemId) {
        itemService.deleteItem(itemId);
    }
}
