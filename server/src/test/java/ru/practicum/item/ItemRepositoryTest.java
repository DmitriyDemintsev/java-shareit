package ru.practicum.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void findAllBySearch() {
        User firstOwner = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        firstOwner = userRepository.save(firstOwner);
        User secondOwner = new User(null, "Петр Петров", "petr@petrov.ru");
        secondOwner = userRepository.save(secondOwner);

        Item firstItem = new Item(null, "дрель", "питание от сети",
                true, firstOwner, null);
        Item secondItem = new Item(null, "аккумуляторная дрель", "два аккумулятора и зарядник",
                true, secondOwner, null);
        firstItem = itemRepository.save(firstItem);
        secondItem = itemRepository.save(secondItem);

        List<Item> expectedItems = List.of(firstItem, secondItem);
        Pageable pageable = PageRequest.of(0, 20);

        Page<Item> actualItem = itemRepository.findAllBySearch("дрель", pageable);
        assertEquals(expectedItems, actualItem.getContent());
    }
}
