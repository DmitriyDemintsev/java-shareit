package ru.practicum.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void getBookingsAllItemsForUserWitchStatus() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);

        Item item = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        item = itemRepository.save(item);

        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageableDesc = PageRequest.of(0, 20, sortByStartDesc);

        Booking booking = new Booking(null, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5),
                item, booker, BookingStatus.WAITING);
        booking = bookingRepository.save(booking);

        List<Booking> expectedBookings = List.of(booking);
        Page<Booking> actualBookings = bookingRepository.getBookingsAllItemsForUserWithStatus(owner,
                BookingStatus.WAITING, pageableDesc);
        assertEquals(expectedBookings, actualBookings.getContent());
    }

    @Test
    void getBookingsAllItemsForUser() {
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

        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageableDesc = PageRequest.of(0, 20, sortByStartDesc);

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
        Page<Booking> actualBookings = bookingRepository.getBookingsAllItemsForUser(owner, pageableDesc);
        assertEquals(expectedBookings, actualBookings.getContent());
    }

    @Test
    void findCompletedBookings() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item firstIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        firstIem = itemRepository.save(firstIem);
        secondItem = itemRepository.save(secondItem);

        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageableDesc = PageRequest.of(0, 20, sortByStartDesc);
        LocalDateTime created = LocalDateTime.now();

        Booking festBooking = new Booking(null, created.minusDays(7), created.minusDays(5),
                firstIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, created.minusDays(3), created.minusDays(1),
                secondItem, booker, BookingStatus.APPROVED);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);

        List<Booking> expectedBookings = List.of(secondBooking, festBooking);
        Page<Booking> actualBookings = bookingRepository.findCompletedBookings(owner, created, pageableDesc);
        assertEquals(expectedBookings, actualBookings.getContent());
    }

    @Test
    void findFutureBookings() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item firstIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        firstIem = itemRepository.save(firstIem);
        secondItem = itemRepository.save(secondItem);

        Sort sortByStartAsc = Sort.by(Sort.Direction.ASC, "start");
        Pageable pageableAsc = PageRequest.of(0, 20, sortByStartAsc);
        LocalDateTime created = LocalDateTime.now();

        Booking festBooking = new Booking(null, created.plusDays(7), created.plusDays(5),
                firstIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, created.plusDays(3), created.plusDays(1),
                secondItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);

        List<Booking> expectedBookings = List.of(secondBooking, festBooking);
        Page<Booking> actualBookings = bookingRepository.findFutureBookings(owner, created, pageableAsc);
        assertEquals(expectedBookings, actualBookings.getContent());
    }

    @Test
    void findCurrentBookings() {
        User booker = new User(null, "Иван Иванов", "ivai@ivanov.ru");
        User owner = new User(null, "Петр Петров", "petr@petrov.ru");
        booker = userRepository.save(booker);
        owner = userRepository.save(owner);
        Item firstIem = new Item(null, "дрель", "питание от сети",
                true, owner, null);
        Item secondItem = new Item(null, "стремянка", "высота 2 метра",
                true, owner, null);
        Item thirdItem = new Item(null, "шуруповерт", "работает от аккумулятора",
                true, owner, null);
        firstIem = itemRepository.save(firstIem);
        secondItem = itemRepository.save(secondItem);
        thirdItem = itemRepository.save(thirdItem);

        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageableDesc = PageRequest.of(0, 20, sortByStartDesc);
        LocalDateTime created = LocalDateTime.now();

        Booking festBooking = new Booking(null, created.minusDays(7), created.plusHours(12),
                firstIem, booker, BookingStatus.APPROVED);
        Booking secondBooking = new Booking(null, created.minusDays(3), created.plusDays(1),
                secondItem, booker, BookingStatus.APPROVED);
        Booking thirdBooking = new Booking(null, created.minusHours(3), created.plusDays(5),
                thirdItem, booker, BookingStatus.WAITING);
        festBooking = bookingRepository.save(festBooking);
        secondBooking = bookingRepository.save(secondBooking);
        thirdBooking = bookingRepository.save(thirdBooking);

        List<Booking> expectedBookings = List.of(thirdBooking, secondBooking, festBooking);
        Page<Booking> actualBookings = bookingRepository.findCurrentBookings(owner, created, created, pageableDesc);
        assertEquals(expectedBookings, actualBookings.getContent());
    }
}
