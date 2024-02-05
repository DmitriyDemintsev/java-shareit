package ru.practicum.user.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class UserMapper {
    public User toUser(UserDto userDto, Long id) {
        User user = new User(
                id,
                userDto.getName(),
                userDto.getEmail());
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
        return userDto;
    }

    public List<UserDto> toUserDtoList(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(toUserDto(user));
        }
        return result;
    }
}
