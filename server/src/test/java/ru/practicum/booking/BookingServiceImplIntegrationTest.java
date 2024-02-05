package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.model.Booking;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrationTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingServiceImpl bookingService;

    @Test
    void getAllBookings_whenBookingStatusIsAll_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getAllBookings(booker.getId(),
                BookingStatusForFilter.ALL, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getAllBookings_whenBookingStatusIsFuture_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(2),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getAllBookings(booker.getId(),
                BookingStatusForFilter.FUTURE, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getAllBookings_whenBookingStatusIsPast_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusMonths(3), LocalDateTime.now().minusMonths(2),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusWeeks(2), LocalDateTime.now().minusWeeks(1),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getAllBookings(booker.getId(),
                BookingStatusForFilter.PAST, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getAllBookings_whenBookingStatusIsCurrent_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusWeeks(1), LocalDateTime.now().plusDays(3),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getAllBookings(booker.getId(),
                BookingStatusForFilter.CURRENT, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getAllBookings_whenBookingStatusIsWaiting_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusWeeks(2), LocalDateTime.now().minusWeeks(1),
                festIem, booker, BookingStatus.WAITING);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                secondItem, booker, BookingStatus.WAITING);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getAllBookings(booker.getId(),
                BookingStatusForFilter.WAITING, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getAllBookings_whenBookingStatusIsRejected_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusWeeks(2), LocalDateTime.now().minusWeeks(1),
                festIem, booker, BookingStatus.REJECTED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                secondItem, booker, BookingStatus.REJECTED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.REJECTED);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getAllBookings(booker.getId(),
                BookingStatusForFilter.REJECTED, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getAllBookings_whenUserNotFound_thenUserNotFoundException() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        userRepository.save(booker);

        assertThrows(UserNotFoundException.class, () -> bookingService.getAllBookings(99L,
                BookingStatusForFilter.REJECTED, 0, 10));
    }

    @Test
    void getBookingsAllItemsForUser_whenBookingStatusIsAll_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusDays(2),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getBookingsAllItemsForUser(owner.getId(),
                BookingStatusForFilter.ALL, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getBookingsAllItemsForUser_whenBookingStatusIsFuture_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(2),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getBookingsAllItemsForUser(owner.getId(),
                BookingStatusForFilter.FUTURE, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getBookingsAllItemsForUser_whenBookingStatusIsPast_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusMonths(3), LocalDateTime.now().minusMonths(2),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusWeeks(2), LocalDateTime.now().minusWeeks(1),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getBookingsAllItemsForUser(owner.getId(),
                BookingStatusForFilter.PAST, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getBookingsAllItemsForUser_whenBookingStatusIsCurrent_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusWeeks(1), LocalDateTime.now().plusDays(3),
                festIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                secondItem, booker, BookingStatus.APPROVED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(festBooking, secondBooking, therdBooking);
        List<Booking> actualBookings = bookingService.getBookingsAllItemsForUser(owner.getId(),
                BookingStatusForFilter.CURRENT, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getBookingsAllItemsForUser_whenBookingStatusIsWaiting_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusWeeks(2), LocalDateTime.now().minusWeeks(1),
                festIem, booker, BookingStatus.WAITING);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                secondItem, booker, BookingStatus.WAITING);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getBookingsAllItemsForUser(owner.getId(),
                BookingStatusForFilter.WAITING, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getBookingsAllItemsForUser_whenBookingStatusIsRejected_thenBookingsReturn() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item festIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        festIem = itemRepository.save(festIem);
        secondItem = itemRepository.save(secondItem);

        Booking festBooking = new Booking(null, LocalDateTime.now().minusWeeks(2), LocalDateTime.now().minusWeeks(1),
                festIem, booker, BookingStatus.REJECTED);
        Booking secondBooking = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2),
                secondItem, booker, BookingStatus.REJECTED);
        Booking therdBooking = new Booking(null, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusDays(3),
                secondItem, booker, BookingStatus.REJECTED);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        therdBooking = bookingRepository.save(therdBooking);

        List<Booking> expectedBookings = List.of(therdBooking, secondBooking, festBooking);
        List<Booking> actualBookings = bookingService.getBookingsAllItemsForUser(owner.getId(),
                BookingStatusForFilter.REJECTED, 0, 10);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void getBookingsAllItemsForUser_whenUserNotFound_thenUserNotFoundException() {
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        userRepository.save(owner);

        assertThrows(UserNotFoundException.class, () -> bookingService.getBookingsAllItemsForUser(99L,
                BookingStatusForFilter.REJECTED, 0, 10));
    }
}
