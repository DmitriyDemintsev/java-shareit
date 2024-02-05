package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.ItemService;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestMapper;
import ru.practicum.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestService itemRequestService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestMapper.toItemRequestDto(itemRequestService.create(userId,
                itemRequestMapper.toItemRequest(itemRequestDto, null)));
    }

    @GetMapping
    public List<ItemRequestDto> getListMineRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemRequestDto> itemRequestDtos = itemRequestMapper
                .toItemRequestDtoList(itemRequestService.getAllMineRequests(userId));
        //данные об ответах
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            addResponse(itemRequestDto);
        }
        return itemRequestDtos;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(value = "from", defaultValue = "0") int from,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        userService.getUserById(userId);
        List<ItemRequestDto> itemRequestDtos = itemRequestMapper
                .toItemRequestDtoList(itemRequestService.getAllRequest(userId, from, size));
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            addResponse(itemRequestDto);
        }
        return itemRequestDtos;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestDtoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable("requestId") long id) {
        userService.getUserById(userId);
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequestService.getRequestById(id));
        //данные об ответах
        addResponse(itemRequestDto);
        return itemRequestDto;
    }

    private void addResponse(ItemRequestDto itemRequestDto) {
        List<Item> items = itemService.getByRequest(itemRequestDto.getId());
        itemRequestDto.setItems(itemMapper.toItemDtoList(items));
    }
}
