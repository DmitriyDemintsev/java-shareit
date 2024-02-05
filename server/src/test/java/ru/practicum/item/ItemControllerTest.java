package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingMapper;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.CommentMapper;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @MockBean
    CommentService commentService;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @SpyBean
    private ItemMapper itemMapper;
    @SpyBean
    private UserMapper userMapper;
    @SpyBean
    private CommentMapper commentMapper;
    @SpyBean
    private BookingMapper bookingMapper;

    private LocalDateTime created = LocalDateTime.now();
    private User owner = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
    private UserDto ownerDto = new UserDto(0L, "Иван Иванов", "ivai@ivanov.ru");
    private User requestor = new User(1L, "Петр Петров", "petr@petrov.ru");
    private UserDto requestorDto = new UserDto(1L, "Петр Петров", "petr@petrov.ru");
    private List<CommentDto> comments = new ArrayList<>();
    private Item item = new Item(0L, "дрель", "дрель аккумуляторная", true,
            owner, null);
    private ItemDto itemDto = new ItemDto(0L, "дрель", "дрель аккумуляторная", true,
            ownerDto.getId(),
            null, null, comments, null);
    private ItemDto createdItemDto = new ItemDto(0L, "дрель", "дрель аккумуляторная", true,
            ownerDto.getId(),
            null, null, null, null);
    private Item updateItem = new Item(0L, "дрель аккумуляторная", "3 аккумулятора + зарядное устройство", false,
            owner, null);
    private ItemDto updateItemDto = new ItemDto(0L, "дрель аккумуляторная", "3 аккумулятора + зарядное устройство", false,
            ownerDto.getId(), null, null, null, null);
    private Item itemForList = new Item(1L, "дрель аккумуляторная", "3 аккумулятора + зарядное устройство", false,
            owner, null);
    private ItemDto itemDtoForList = new ItemDto(1L, "дрель аккумуляторная", "3 аккумулятора + зарядное устройство", false,
            ownerDto.getId(), null, null, comments, null);
    private ItemDto itemDtoForSearch = new ItemDto(1L, "дрель аккумуляторная", "3 аккумулятора + зарядное устройство", false,
            ownerDto.getId(), null, null, null, null);

    private ItemRequest request = new ItemRequest(0L, "срочно нужен аккумуляторный шуруповерт",
            requestor, created);
    private BookingDto nextBookingDto = new BookingDto();
    private BookingDto lastBookingDto = new BookingDto();

    @Test
    void createItem() throws Exception {
        when(itemService.create(anyLong(), any(), any())).thenReturn(item);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(createdItemDto.getName())))
                .andExpect(jsonPath("$.description", is(createdItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(createdItemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking", is(nextBookingDto.getBooker())))
                .andExpect(jsonPath("$.lastBooking", is(lastBookingDto.getBooker())))
                .andExpect(jsonPath("$.comments", is(createdItemDto.getComments())))
                .andExpect(jsonPath("$.requestId", is(createdItemDto.getRequestId()), Long.class));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.update(anyLong(), any())).thenReturn(updateItem);

        mvc.perform(patch("/items/{id}", ownerDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateItemDto.getName())))
                .andExpect(jsonPath("$.description", is(updateItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(updateItemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking", is(nextBookingDto.getBooker())))
                .andExpect(jsonPath("$.lastBooking", is(lastBookingDto.getBooker())))
                .andExpect(jsonPath("$.comments", is(updateItemDto.getComments())))
                .andExpect(jsonPath("$.requestId", is(updateItemDto.getRequestId()), Long.class));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/{itemId}", ownerDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItems() throws Exception {
        List<Item> items = List.of(item, itemForList);
        when(itemService.getItems(anyLong(), anyInt(), anyInt())).thenReturn(items);

        mvc.perform(get("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].owner", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking", is(nextBookingDto.getBooker())))
                .andExpect(jsonPath("$[0].lastBooking", is(lastBookingDto.getBooker())))
                .andExpect(jsonPath("$[0].comments", is(itemDto.getComments())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(itemDtoForList.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(itemDtoForList.getName())))
                .andExpect(jsonPath("$[1].description", is(itemDtoForList.getDescription())))
                .andExpect(jsonPath("$[1].available", is(itemDtoForList.getAvailable())))
                .andExpect(jsonPath("$[1].owner", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].nextBooking", is(nextBookingDto.getBooker())))
                .andExpect(jsonPath("$[1].lastBooking", is(lastBookingDto.getBooker())))
                .andExpect(jsonPath("$[1].comments", is(itemDtoForList.getComments())))
                .andExpect(jsonPath("$[1].requestId", is(itemDtoForList.getRequestId()), Long.class));
    }

    @Test
    void getItemDtoById() throws Exception {
        when(itemService.getItemById(anyLong())).thenReturn(item);

        mvc.perform(get("/items/{id}", ownerDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking", is(nextBookingDto.getBooker())))
                .andExpect(jsonPath("$.lastBooking", is(lastBookingDto.getBooker())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void getSearchItem() throws Exception {
        List<Item> items = List.of(item, itemForList);
        when(itemService.getItemsBySearch(anyString(), anyInt(), anyInt())).thenReturn(items);

        mvc.perform(get("/items/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("text", item.getName(), item.getDescription()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(createdItemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(createdItemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(createdItemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(createdItemDto.getAvailable())))
                .andExpect(jsonPath("$[0].owner", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking", is(nextBookingDto.getBooker())))
                .andExpect(jsonPath("$[0].lastBooking", is(lastBookingDto.getBooker())))
                .andExpect(jsonPath("$[0].comments", is(createdItemDto.getComments())))
                .andExpect(jsonPath("$[0].requestId", is(createdItemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(itemDtoForSearch.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(itemDtoForSearch.getName())))
                .andExpect(jsonPath("$[1].description", is(itemDtoForSearch.getDescription())))
                .andExpect(jsonPath("$[1].available", is(itemDtoForSearch.getAvailable())))
                .andExpect(jsonPath("$[1].owner", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$[1].nextBooking", is(nextBookingDto.getBooker())))
                .andExpect(jsonPath("$[1].lastBooking", is(lastBookingDto.getBooker())))
                .andExpect(jsonPath("$[1].comments", is(itemDtoForSearch.getComments())))
                .andExpect(jsonPath("$[1].requestId", is(itemDtoForSearch.getRequestId()), Long.class));
    }
}
