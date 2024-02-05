package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.List;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userMapper.toUserDtoList(userService.getAllUsers());
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userMapper.toUserDto(userService.create(userMapper.toUser(userDto, null)));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody UserDto userDto,
                              @PathVariable("id") long id) {
        return userMapper.toUserDto(userService.update(userMapper.toUser(userDto, id)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDto getUserDtoById(@PathVariable long id) {
        return userMapper.toUserDto(userService.getUserById(id));
    }
}
