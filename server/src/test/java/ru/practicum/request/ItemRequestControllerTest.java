package ru.practicum.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.dto.BookingMapper;
import ru.practicum.item.CommentService;
import ru.practicum.item.ItemService;
import ru.practicum.item.dto.CommentMapper;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.MatchingUtils.isLocalDateTime;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    @MockBean
    ItemService itemService;
    @MockBean
    UserService userService;
    @MockBean
    CommentService commentService;
    @MockBean
    BookingService bookingService;
    @SpyBean
    private ItemRequestMapper itemRequestMapper;
    @SpyBean
    private ItemMapper itemMapper;
    @SpyBean
    private UserMapper userMapper;
    @SpyBean
    private CommentMapper commentMapper;
    @SpyBean
    private BookingMapper bookingMapper;

    private User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
    private UserDto userDto = new UserDto(0L, "Иван Иванов", "ivai@ivanov.ru");
    private User anotherOne = new User(1L, "Петр Петров", "petr@petrov.ru");
    private UserDto anotherOneDto = new UserDto(1L, "Петр Петров", "petr@petrov.ru");
    private User anotherTwo = new User(1L, "Сидор Сидоров", "sidor@sidorov.ru");
    private UserDto anotherTwoDto = new UserDto(1L, "Сидор Сидоров", "sidor@sidorov.ru");
    private LocalDateTime created = LocalDateTime.now();
    private List<ItemDto> items = new ArrayList<>();
    private List<ItemDto> itemsOne = new ArrayList<>();
    private List<ItemDto> itemsTwo = new ArrayList<>();
    private ItemRequest request = new ItemRequest(0L, "срочно нужен аккумуляторный шуруповерт",
            user, created);
    private ItemRequestDto requestDto = new ItemRequestDto(0L, "срочно нужен аккумуляторный шуруповерт", created,
            items);
    private ItemRequestDto createdRequestDto = new ItemRequestDto(0L, "срочно нужен аккумуляторный шуруповерт", created,
            null);
    private ItemRequest requestForList = new ItemRequest(3L, "нужна гитара для выступления",
            user, created.plusDays(1));
    private ItemRequestDto requestDtoForList = new ItemRequestDto(3L, "нужна гитара для выступления", created.plusDays(1),
            items);

    private ItemRequest requestOne = new ItemRequest(1L, "ищу стремянку, высота - 2 метра",
            anotherOne, created.plusDays(1));
    private ItemRequestDto requestDtoOne = new ItemRequestDto(1L, "ищу стремянку, высота - 2 метра", created.plusDays(1),
            items);
    private ItemRequest requestTwo = new ItemRequest(2L, "кто одолжит перфоратор на неделю?",
            anotherTwo, created.plusDays(1));
    private ItemRequestDto requestDtoTwo = new ItemRequestDto(2L, "кто одолжит перфоратор на неделю?", created.plusDays(1),
            items);

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.create(anyLong(), any())).thenReturn(request);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(createdRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", isLocalDateTime(createdRequestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(createdRequestDto.getItems())));
    }

    @Test
    void getListMineRequests() throws Exception {
        List<ItemRequest> itemRequests = List.of(request, requestForList);
        when(itemRequestService.getAllMineRequests(anyLong())).thenReturn(itemRequests);

        mvc.perform(get("/requests", userDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].created", isLocalDateTime(requestDto.getCreated())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())))
                .andExpect(jsonPath("$[1].id", is(requestDtoForList.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(requestDtoForList.getDescription())))
                .andExpect(jsonPath("$[1].created", isLocalDateTime(requestDtoForList.getCreated())))
                .andExpect(jsonPath("$[1].items", is(requestDtoForList.getItems())));
    }

    @Test
    void getAll() throws Exception {
        List<ItemRequest> itemRequests = List.of(requestOne, requestTwo);
        when(itemRequestService.getAllRequest(anyLong(), anyInt(), anyInt())).thenReturn(itemRequests);

        mvc.perform(get("/requests/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDtoOne.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoOne.getDescription())))
                .andExpect(jsonPath("$[0].created", isLocalDateTime(requestDtoOne.getCreated())))
                .andExpect(jsonPath("$[0].items", is(requestDtoOne.getItems())))
                .andExpect(jsonPath("$[1].id", is(requestDtoTwo.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(requestDtoTwo.getDescription())))
                .andExpect(jsonPath("$[1].created", isLocalDateTime(requestDtoTwo.getCreated())))
                .andExpect(jsonPath("$[1].items", is(requestDtoTwo.getItems())));
    }

    @Test
    void getItemRequestDtoById() throws Exception {
        when(itemRequestService.getRequestById(anyLong())).thenReturn(request);

        mvc.perform(get("/requests/{requestId}", userDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", isLocalDateTime(requestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }
}
