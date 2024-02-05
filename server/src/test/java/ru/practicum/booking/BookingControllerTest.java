package ru.practicum.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.dto.CommentMapper;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.MatchingUtils.isLocalDateTime;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @SpyBean
    private BookingMapper bookingMapper;
    @SpyBean
    private CommentMapper commentMapper;
    @SpyBean
    private ItemMapper itemMapper;
    @SpyBean
    private UserMapper userMapper;

    private User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
    private UserDto ownerDto = new UserDto(0L, "Иван Иванов", "ivai@ivanov.ru");
    private User booker = new User(1L, "Петр Петров", "petr@petrov.ru");
    private UserDto bookerDto = new UserDto(1L, "Петр Петров", "petr@petrov.ru");
    private Item item = new Item(0L, "дрель", "питание от сети",
            true, owner, null);
    private ItemDto itemDto = new ItemDto(0L, "дрель", "питание от сети", true, owner.getId(),
            null, null, null, null);
    private LocalDateTime current = LocalDateTime.now();
    private Booking booking = new Booking(0L, current,
            current.minusDays(1), item, booker, BookingStatus.WAITING);
    private BookingDto bookingDto = new BookingDto(0L, current,
            current.minusDays(1), itemDto, ownerDto, bookerDto.getId(), BookingStatus.WAITING);
    private Booking updateBooking = new Booking(0L, current,
            current.minusDays(1), item, booker, BookingStatus.APPROVED);
    private BookingDto updateBookingDto = new BookingDto(0L, current,
            current.minusDays(1), itemDto, ownerDto, bookerDto.getId(), BookingStatus.APPROVED);
    private Booking bookingForList = new Booking(0L, current,
            current.minusDays(1), item, booker, BookingStatus.WAITING);

    private BookingDto bookingDtoForList = new BookingDto(0L, current,
            current.minusDays(1), itemDto, ownerDto, bookerDto.getId(), BookingStatus.WAITING);


    @Test
    void createBooking() throws Exception {
        when(bookingService.create(any(), anyLong(), any())).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookerDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", isLocalDateTime(bookingDto.getStart())))
                .andExpect(jsonPath("$.end", isLocalDateTime(bookingDto.getEnd())))
                .andExpect(jsonPath("$.item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingService.update(anyLong(), anyBoolean(), anyLong())).thenReturn(updateBooking);

        mvc.perform(patch("/bookings/{id}", ownerDto.getId())
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", isLocalDateTime(updateBookingDto.getStart())))
                .andExpect(jsonPath("$.end", isLocalDateTime(bookingDto.getEnd())))
                .andExpect(jsonPath("$.item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(updateBookingDto.getStatus().toString())));
    }

    @Test
    void deleteBooking() throws Exception {
        mvc.perform(delete("/bookings/{id}", ownerDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingDtoById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(booking);

        mvc.perform(get("/bookings/{id}", ownerDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", isLocalDateTime(bookingDto.getStart())))
                .andExpect(jsonPath("$.end", isLocalDateTime(bookingDto.getEnd())))
                .andExpect(jsonPath("$.item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));

    }

    @Test
    void getAllBookingsForUser() throws Exception {
        List<Booking> bookings = List.of(booking, bookingForList);
        when(bookingService.getAllBookings(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookings);

        mvc.perform(get("/bookings")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", isLocalDateTime(bookingDto.getStart())))
                .andExpect(jsonPath("$[0].end", isLocalDateTime(bookingDto.getEnd())))
                .andExpect(jsonPath("$[0].item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].bookerId", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$[1].id", is(bookingDtoForList.getId()), Long.class))
                .andExpect(jsonPath("$[1].start", isLocalDateTime(bookingDtoForList.getStart())))
                .andExpect(jsonPath("$[1].end", isLocalDateTime(bookingDtoForList.getEnd())))
                .andExpect(jsonPath("$[1].item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].booker.id", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].bookerId", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].status", is(bookingDtoForList.getStatus().toString())));
    }

    @Test
    void getBookingsAllItemsForUser() throws Exception {
        List<Booking> bookings = List.of(booking, bookingForList);
        when(bookingService.getBookingsAllItemsForUser(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookings);

        mvc.perform(get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", isLocalDateTime(bookingDto.getStart())))
                .andExpect(jsonPath("$[0].end", isLocalDateTime(bookingDto.getEnd())))
                .andExpect(jsonPath("$[0].item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].bookerId", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$[1].id", is(bookingDtoForList.getId()), Long.class))
                .andExpect(jsonPath("$[1].start", isLocalDateTime(bookingDtoForList.getStart())))
                .andExpect(jsonPath("$[1].end", isLocalDateTime(bookingDtoForList.getEnd())))
                .andExpect(jsonPath("$[1].item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].booker.id", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].bookerId", is(bookerDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].status", is(bookingDtoForList.getStatus().toString())));
    }
}
