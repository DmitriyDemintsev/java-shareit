package ru.practicum.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.model.Item;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemService {

    List<Item> getItems(long userId, int from, int size);

    @Transactional
    Item create(Long userId, Long requestId, Item item);

    @Transactional
    Item update(Long userId, Item item);

    @Transactional
    void deleteItem(long itemId);

    List<Item> findAllItems();

    Item getItemById(long id);

    List<Item> getItemsBySearch(String query, int from, int size);

    List<Item> getByRequest(long requestId);
}
