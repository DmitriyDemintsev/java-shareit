package ru.practicum.request;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemRequestService {

    @Transactional
    ItemRequest create(Long userId, ItemRequest itemRequest);

    List<ItemRequest> getAllMineRequests(long id);

    List<ItemRequest> getAllRequest(long userId, int from, int size);

    ItemRequest getRequestById(long id);
}
