package ru.practicum.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item save(Item item); //создать и обновить

    void deleteById(long itemId); //удалить

    Page<Item> findByOwner(User userOwner, Pageable pageable); //все вещи владельца по владельцу

    List<Item> findByRequest(ItemRequest request, Sort sort); //все вещи по запросу

    Item getItemById(long id); //вещь по её id

    List<Item> findAll(); // вообще все вещи

    @Query(" select i from Item i join i.owner as u " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) " +
            "and available = true")
    Page<Item> findAllBySearch(String query, Pageable pageable); //для поиска по запросу
}
