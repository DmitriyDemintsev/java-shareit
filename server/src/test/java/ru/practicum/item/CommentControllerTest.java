package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.CommentMapper;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.MatchingUtils.isLocalDateTime;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    CommentService commentService;
    @Autowired
    private MockMvc mvc;
    @SpyBean
    private CommentMapper commentMapper;
    @SpyBean
    private ItemMapper itemMapper;
    @SpyBean
    private UserMapper userMapper;

    private LocalDateTime created = LocalDateTime.now();
    private User author = new User(0L, "null", null);
    private UserDto authorDto = new UserDto(0L, "null", null);
    private Item item = new Item(0L, null, null, null, author,
            null);
    private ItemDto itemDto = new ItemDto(0L, null, null, null, authorDto.getId(),
            null, null, null, null);
    private Comment comment = new Comment(0L, "вся правда", item, author, created);
    private CommentDto commentDto = new CommentDto(0L, "вся правда", itemDto, authorDto, authorDto.getName(), created);

    @Test
    void createComment() throws Exception {
        when(commentService.create(anyLong(), anyLong(), any())).thenReturn(comment);

        mvc.perform(post("/items/{itemId}/comment", itemDto.getId())
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", authorDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.item.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.author.id", is(authorDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", isLocalDateTime(commentDto.getCreated())));
    }
}
