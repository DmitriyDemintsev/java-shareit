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
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingServiceImpl;
import ru.practicum.booking.model.Booking;
import ru.practicum.exception.ItemAlreadyExistException;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.ItemValidationException;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingServiceImpl bookingService;
    @Mock
    private CommentServiceImpl commentService;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @Test
    void create_whenItemValid_thenSavedItem() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Item savedItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, user, null, null, null, null);
        when(itemRepository.save(savedItem)).thenReturn(savedItem);

        Item actualItem = itemService.create(user.getId(), null, savedItem);

        assertEquals(savedItem, actualItem);
        verify(itemRepository).save(savedItem);
    }

    @Test
    void create_whenItemNameNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        Item savedItem = new Item(0L, "", "дрель аккумуляторная",
                true, user, null, null, null, null);

        assertThrows(ItemValidationException.class, () -> itemService.create(user.getId(), null, savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void create_whenItemDescriptionNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        Item savedItem = new Item(0L, "дрель", "",
                true, user, null, null, null, null);

        assertThrows(ItemValidationException.class, () -> itemService.create(user.getId(), null, savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void create_whenItemAvailableNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        Item savedItem = new Item(0L, "дрель", "дрель аккумуляторная",
                null, user, null, null, null, null);

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
                true, user, null, null, null, null);
        when(itemRepository.getItemById(savedItem.getId())).thenReturn(savedItem);

        assertThrows(ItemValidationException.class, () -> itemService.update(user.getId(), savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void update_whenItemDescriptionNotValid_thenItemValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Item savedItem = new Item(0L, "дрель", "",
                true, user, null, null, null, null);
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
                true, trueUser, null, null, null, null);
        when(itemRepository.getItemById(savedItem.getId())).thenReturn(savedItem);

        assertThrows(ItemAlreadyExistException.class, () -> itemService.update(folseUser.getId(), savedItem));
        verify(itemRepository, never()).save(savedItem);
    }

    @Test
    void getItemById_whenItemFound_thenReturnedItem() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        List<Comment> comments = new ArrayList<>();

        Booking last = new Booking();
        Booking next = new Booking();

        Item expectedItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, user, next, last, comments, null);
        when(commentService.getComments(expectedItem.getId())).thenReturn(comments);
        when(bookingService.getNext(expectedItem.getId())).thenReturn(next);
        when(bookingService.getLast(expectedItem.getId())).thenReturn(last);
        when(itemRepository.findById(expectedItem.getId())).thenReturn(Optional.of(expectedItem));

        Item actualItem = itemService.getItemById(user.getId(), expectedItem.getId());

        assertEquals(expectedItem, actualItem);
    }

    @Test
    void getItemById_whenItemNotFound_thenItemNotFoundException() {
        long userId = 0L;
        long id = 0L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(userId, id));
    }

    @Test
    void getItems_whenUserValid_thenReturnedItems() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<Comment> comment = new ArrayList<>();

        Item fistItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, user, null, null, comment, null);
        Item secondItem = new Item(1L, "набор отверток", "отвертки под разные шлицы",
                true, user, null, null, comment, null);

        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(fistItem);
        expectedItems.add(secondItem);

        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sortById);

        when(commentService.getComments(fistItem.getId())).thenReturn(comment);
        when(commentService.getComments(secondItem.getId())).thenReturn(comment);
        when(itemRepository.findByOwner(user, pageable)).thenReturn(new PageImpl<>(expectedItems));

        List<Item> actualItems = itemService.getItems(user.getId(), 1, 10);

        assertEquals(expectedItems, actualItems);
    }

    @Test
    void findAllItems() {
        User fistUser = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User secondUser = new User(1L, "Петр Петров", "petr@petrov.ru");
        User thirdUser = new User(2L, "Степан Степанов", "stepan@stepanov.ru");

        Item fistItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, fistUser, null, null, null, null);
        Item secondItem = new Item(1L, "набор отверток", "отвертки под разные шлицы",
                true, secondUser, null, null, null, null);
        Item thirddItem = new Item(1L, "стремянка", "высота 2 метра",
                true, thirdUser, null, null, null, null);

        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(fistItem);
        expectedItems.add(secondItem);
        expectedItems.add(thirddItem);

        when(itemRepository.findAll()).thenReturn(expectedItems);

        List<Item> actualItems = itemService.findAllItems();

        assertEquals(expectedItems, actualItems);

    }

    @Test
    void deleteItem() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        Item deletedItem = new Item(0L, "дрель", "дрель аккумуляторная",
                true, user, null, null, null, null);

        itemRepository.deleteById(deletedItem.getId());
        assertNull(itemRepository.getItemById(deletedItem.getId()));
    }
}
