package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemServiceImpl itemService;

    @Test
    public void getItems_whenUserIsFound_thenItemsReturn() {
        User owner = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        owner = userRepository.save(owner);

        Item firstIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "гитара", "есть чехол и каподастр",
                true, owner, null);
        firstIem = itemRepository.save(firstIem);
        secondItem = itemRepository.save(secondItem);

        List<Item> expectedItems = List.of(firstIem, secondItem);
        List<Item> actualItems = itemService.getItems(owner.getId(), 1, 10);

        assertEquals(expectedItems, actualItems);
    }

    @Test
    public void getItems_whenUserIsNotFound_thenUserNotFoundException() {
        User owner = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        userRepository.save(owner);

        assertThrows(UserNotFoundException.class, () -> itemService.getItems(99, 1, 10));
    }

    @Test
    public void getItemsBySearch() {
        User owner = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        owner = userRepository.save(owner);
        Item firstIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "аккумуляторная дрель", "два аккумулятора и зарядник",
                true, owner, null);

        firstIem = itemRepository.save(firstIem);
        secondItem = itemRepository.save(secondItem);

        List<Item> expectedItems = List.of(firstIem, secondItem);
        List<Item> actualItems = itemService.getItemsBySearch("дрель", 1, 10);

        assertEquals(expectedItems, actualItems);
    }
}
