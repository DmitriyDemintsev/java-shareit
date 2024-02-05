package ru.practicum.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.booking.model.Booking;
import ru.practicum.exception.BookingNotFoundException;
import ru.practicum.exception.BookingValidationException;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @Test
    void create_whenBookingValid_thenSavedBooking() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, user, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking savedBooking = new Booking(0L, current.plusDays(1),
                current.plusDays(2), item, booker, BookingStatus.APPROVED);
        when(bookingRepository.save(savedBooking)).thenReturn(savedBooking);

        Booking actualBooking = bookingService.create(savedBooking, booker.getId(),
                item.getId());

        assertEquals(savedBooking, actualBooking);
        verify(bookingRepository).save(savedBooking);
    }

    @Test
    void create_whenBookingStartIsNull_thenBookingValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, user, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking booking = new Booking(0L, null,
                current.plusDays(2), item, booker, BookingStatus.APPROVED);

        assertThrows(BookingValidationException.class,
                () -> bookingService.create(booking, booker.getId(), item.getId()));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenBookingStartIsBeforeEnd_thenBookingValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, user, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking booking = new Booking(0L, current,
                current.minusDays(1), item, booker, BookingStatus.APPROVED);

        assertThrows(BookingValidationException.class,
                () -> bookingService.create(booking, booker.getId(), item.getId()));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenBookingEndIsNull_thenBookingValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, user, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking booking = new Booking(0L, current,
                null, item, booker, BookingStatus.APPROVED);

        assertThrows(BookingValidationException.class,
                () -> bookingService.create(booking, booker.getId(), item.getId()));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenBookingEndIsAfterStart_thenBookingValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, user, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking booking = new Booking(0L, current,
                current.minusDays(1), item, booker, BookingStatus.APPROVED);

        assertThrows(BookingValidationException.class,
                () -> bookingService.create(booking, booker.getId(), item.getId()));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenItemBeingBookedIsNotAvailable_thenBookingValidationException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                false, user, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking booking = new Booking(0L, current,
                current.plusDays(1), item, booker, BookingStatus.APPROVED);

        assertThrows(BookingValidationException.class,
                () -> bookingService.create(booking, booker.getId(), item.getId()));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenBookingIsNotAvailable_thenBookingNotFoundException() {
        User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking booking = new Booking(0L, current.plusDays(1),
                current.plusDays(2), item, booker, BookingStatus.APPROVED);

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.create(booking, booker.getId(), item.getId()));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void update_whenBookingFound_thenUpdatedOnlyAvailableFields() {
        User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking oldBooking = new Booking(0L, current.plusDays(1),
                current.plusDays(2), item, booker, BookingStatus.WAITING);
        Booking newBooking = new Booking(0L, current.plusDays(1),
                current.plusDays(2), item, booker, BookingStatus.APPROVED);

        when(bookingRepository.findById(oldBooking.getId())).thenReturn(Optional.of(oldBooking));

        Booking actualBooking = bookingService.update(oldBooking.getId(), true, owner.getId());

        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking saveBooking = bookingArgumentCaptor.getValue();

        assertEquals(BookingStatus.APPROVED, saveBooking.getStatus());
    }

    @Test
    void update_whenBookingNotOwner_thenBookingNotFoundException() {
        User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking savedBooking = new Booking(0L, current.plusDays(1),
                current.plusDays(2), item, booker, BookingStatus.WAITING);
        when(bookingRepository.findById(savedBooking.getId())).thenReturn(Optional.of(savedBooking));

        assertThrows(BookingNotFoundException.class, () -> bookingService.update(savedBooking.getId(), true, booker.getId()));
        verify(bookingRepository, never()).save(savedBooking);
    }

    @Test
    void update_whenBookingStatusNotValid_thenBookingValidationException() {
        User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Booking savedBooking = new Booking(0L, current.plusDays(1),
                current.plusDays(2), item, booker, BookingStatus.CANCELED);
        when(bookingRepository.findById(savedBooking.getId())).thenReturn(Optional.of(savedBooking));

        assertThrows(BookingValidationException.class, () -> bookingService.update(savedBooking.getId(), true, owner.getId()));
        verify(bookingRepository, never()).save(savedBooking);
    }

    @Test
    void getBookingById_whenBookingFound_thenBookingReturn() {
        User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);

        LocalDateTime current = LocalDateTime.now();
        Booking expectedBooking = new Booking(0L, current.plusDays(1),
                current.plusDays(2), item, booker, BookingStatus.APPROVED);
        when(bookingRepository.findById(expectedBooking.getId())).thenReturn(Optional.of(expectedBooking));

        Booking actualBooking = bookingService.getBookingById(booker.getId(),
                item.getId());

        assertEquals(expectedBooking, actualBooking);
    }

    @Test
    void getBookingById_whenBookingNotFound_thenBookingNotFoundException() {
        long userId = 0L;
        long bookingId = 0L;
        when(bookingRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(userId, bookingId));
    }

    @Test
    void getBookingById_whenBookingIsNotAvailable_thenBookingNotFoundException() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
        User owner = new User(2L, "Петр Петров", "petr@petrov.ru");

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);

        Booking booking = new Booking(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item, booker, BookingStatus.APPROVED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(user.getId(), booking.getId()));
    }
}
