package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mvc;
    @SpyBean
    private UserMapper userMapper;

    private User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
    private User updateUser = new User(0L, "Петр Петров", "petr@petrov.ru");
    private User userForList = new User(1L, "Петр Петров", "petr@petrov.ru");
    private UserDto userDto = new UserDto(0L, "Иван Иванов", "ivai@ivanov.ru");
    private UserDto updateUserDto = new UserDto(0L, "Петр Петров", "petr@petrov.ru");
    private UserDto userDtoForList = new UserDto(1L, "Петр Петров", "petr@petrov.ru");

    @Test
    void createUser() throws Exception {
        when(userService.create(any())).thenReturn(user);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.update(any())).thenReturn(updateUser);

        mvc.perform(patch("/users/{id}", userDto.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateUserDto.getName())))
                .andExpect(jsonPath("$.email", is(updateUserDto.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/{id}", userDto.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserDtoById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(user);

        mvc.perform(get("/users/{id}", userDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users = List.of(user, userForList);
        when(userService.getAllUsers()).thenReturn(users);

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$[1].id", is(userDtoForList.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(userDtoForList.getName())))
                .andExpect(jsonPath("$[1].email", is(userDtoForList.getEmail())));
    }
}
