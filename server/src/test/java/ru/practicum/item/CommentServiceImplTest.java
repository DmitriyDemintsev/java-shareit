package ru.practicum.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingServiceImpl;
import ru.practicum.booking.BookingStatus;
import ru.practicum.booking.BookingStatusForFilter;
import ru.practicum.booking.model.Booking;
import ru.practicum.exception.BookingValidationException;
import ru.practicum.exception.ItemValidationException;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    @Captor
    private ArgumentCaptor<Comment> userArgumentCaptor;

    @Test
    void create_whenCommentValid_thenSavedComment() {
        BookingServiceImpl mockBookingService = Mockito.mock(BookingServiceImpl.class);

        User author = new User(0L, "Иван Иванов", "ivan@ivanov.ru"); //он же booker
        User owner = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime start = current.minusDays(5);
        LocalDateTime end = current.minusDays(3);

        Booking booking = new Booking(0L, start, end, item, author, BookingStatus.APPROVED);
        bookingRepository.save(booking);
        mockBookingService.getAllBookings(author.getId(),
                BookingStatusForFilter.PAST, 1, 1);

        when(bookingRepository.findByBookerAndEndIsBeforeAndItem(any(), any(), any()))
                .thenReturn(List.of(booking));

        Comment savedComment = new Comment(0L, "вся правда об использовании дрели", item, author, current);
        when(commentRepository.save(savedComment)).thenReturn(savedComment);
        System.out.println("savedComment " + savedComment);

        Comment actualComment = commentService.create(author.getId(), item.getId(), savedComment);
        System.out.println("actualComment " + actualComment);

        assertEquals(savedComment, actualComment);
        verify(commentRepository).save(savedComment);
    }

    @Test
    void create_whenBookingStartNotValid_thenBookingValidationException() {

        BookingServiceImpl mockBookingServiceImpl = Mockito.mock(BookingServiceImpl.class);

        User author = new User(0L, "Иван Иванов", "ivai@ivanov.ru"); //он же booker
        User owner = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();

        Booking booking = new Booking(0L, current.minusDays(5), current.minusDays(2),
                item, author, BookingStatus.APPROVED);
        //      when(mockBookingServiceImpl.getAllBookings(author.getId(), BookingStatusForFilter.PAST, 1, 1));


        Comment comment = new Comment(0L, "вся правда об использовании дрели", item, author, current);

        assertThrows(BookingValidationException.class,
                () -> commentService.create(author.getId(), item.getId(), comment));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void create_whenCommentIsNull_thenBookingValidationException() {
        User author = new User(0L, "Иван Иванов", "ivai@ivanov.ru"); //он же booker
        User owner = new User(1L, "Петр Петров", "petr@petrov.ru");
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));

        Item item = new Item(0L, "дрель", "питание от сети",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        LocalDateTime current = LocalDateTime.now();
        Comment comment = new Comment(0L, null, item, author, current);

        assertThrows(BookingValidationException.class,
                () -> commentService.create(author.getId(), item.getId(), comment));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void getComments_whenItemValid_thenSavedComment() {
        User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        Item item = new Item(0L, "дрель", "дрель аккумуляторная",
                true, owner, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        Comment fistComment = new Comment();
        Comment secondComment = new Comment();
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(fistComment);
        expectedComments.add(secondComment);

        when(commentRepository.findAllByItem(item)).thenReturn(expectedComments);
        List<Comment> actualComments = commentService.getComments(item.getId());
        assertEquals(expectedComments, actualComments);
    }

    @Test
    void getComments_whenItemNotValid_thenItemValidationException() {
        long id = 0L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemValidationException.class, () -> commentService.getComments(id));
    }
}
