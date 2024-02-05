package ru.practicum.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.ItemAlreadyExistException;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.ItemValidationException;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @Test
    void create_whenItemValid_thenSavedItem() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Item savedItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, user, null);
        when(itemRepository.save(savedItem)).thenReturn(savedItem);

        Item actualItem = itemService.create(user.getId(), null, savedItem);

        assertEquals(savedItem, actualItem);
        verify(itemRepository).save(savedItem);
    }

    @Test
    void create_whenItemNameNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        Item savedItem = new Item(0L, "", "дрель аккумуляторная",
                true, user, null);

        assertThrows(ItemValidationException.class, () -> itemService.create(user.getId(), null, savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void create_whenItemDescriptionNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        Item savedItem = new Item(0L, "дрель", "",
                true, user, null);

        assertThrows(ItemValidationException.class, () -> itemService.create(user.getId(), null, savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void create_whenItemAvailableNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        Item savedItem = new Item(0L, "дрель", "дрель аккумуляторная",
                null, user, null);

        assertThrows(ItemValidationException.class, () -> itemService.create(user.getId(), null, savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void update_whenItemFound_thenUpdatedOnlyAvailableFields() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Long itemId = 0L;
        Item oldItem = new Item();
        oldItem.setId(itemId);
        oldItem.setName("дрель");
        oldItem.setDescription("питание от сети");
        oldItem.setAvailable(false);
        oldItem.setOwner(user);
        oldItem.setRequest(null);

        Item newItem = new Item();
        newItem.setId(oldItem.getId());
        newItem.setName("шуруповерт");
        newItem.setDescription("работает от аккумулятора");
        newItem.setAvailable(true);
        newItem.setOwner(user);
        newItem.setRequest(null);

        when(itemRepository.getItemById(itemId)).thenReturn(oldItem);

        Item actualItem = itemService.update(user.getId(), newItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savesItem = itemArgumentCaptor.getValue();

        assertEquals("шуруповерт", savesItem.getName());
        assertEquals("работает от аккумулятора", savesItem.getDescription());
        assertEquals(true, savesItem.getAvailable());
    }

    @Test
    void update_whenItemNameNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Item savedItem = new Item(0L, "", "дрель аккумуляторная",
                true, user, null);
        when(itemRepository.getItemById(savedItem.getId())).thenReturn(savedItem);

        assertThrows(ItemValidationException.class, () -> itemService.update(user.getId(), savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void update_whenItemDescriptionNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Item savedItem = new Item(0L, "дрель", "",
                true, user, null);
        when(itemRepository.getItemById(savedItem.getId())).thenReturn(savedItem);

        assertThrows(ItemValidationException.class, () -> itemService.update(user.getId(), savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void update_whenItemOwnerNotValid_thenItemAlreadyExistException() {
        User trueUser = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User folseUser = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(folseUser.getId())).thenReturn(Optional.of(folseUser));

        Item savedItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, trueUser, null);
        when(itemRepository.getItemById(savedItem.getId())).thenReturn(savedItem);

        assertThrows(ItemAlreadyExistException.class, () -> itemService.update(folseUser.getId(), savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void getItemById_whenItemFound_thenReturnedItem() {
        long id = 0L;
        Item expectedItem = new Item();
        when(itemRepository.findById(id)).thenReturn(Optional.of(expectedItem));

        Item actualItem = itemService.getItemById(id);

        assertEquals(expectedItem, actualItem);
    }

    @Test
    void getItemById_whenItemNotFound_thenItemNotFoundException() {
        long id = 0L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(id));
    }

    @Test
    void getItems() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Item fistItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, user, null);
        Item secondItem = new Item(1L, "набор отверток", "отвертки под разные шлицы",
                true, user, null);

        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(fistItem);
        expectedItems.add(secondItem);

        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sortById);

        when(itemRepository.findByOwner(user, pageable)).thenReturn(new PageImpl<>(expectedItems));

        List<Item> actualItems = itemService.getItems(user.getId(), 1, 10);

        assertEquals(expectedItems, actualItems);
    }
}
