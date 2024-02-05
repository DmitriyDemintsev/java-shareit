package ru.practicum.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.UserValidationException;
import ru.practicum.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void create_whenUserNameValid_thenSavedUser() {
        User savedUser = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.save(savedUser)).thenReturn(savedUser);

        User actualUser = userService.create(savedUser);

        assertEquals(savedUser, actualUser);
        verify(userRepository).save(savedUser);
    }

    @Test
    void create_whenUserNameNotValid_thenUserValidationException() {
        User savedUser = new User(0L, "", "ivai@ivanov.ru");

        assertThrows(UserValidationException.class, () -> userService.create(savedUser));
        verify(userRepository, never()).save(savedUser);
    }

    @Test
    void create_whenUserEmailNotValid_thenUserValidationException() {
        User savedUser = new User(0L, "Иван Иванов", "");

        assertThrows(UserValidationException.class, () -> userService.create(savedUser));
        verify(userRepository, never()).save(savedUser);
    }

    @Test
    void update_whenUserFound_thenUpdatedOnlyAvailableFields() {
        Long userId = 0L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("Иван Иванов");
        oldUser.setEmail("ivai@ivanov.ru");

        User newUser = new User();
        newUser.setId(oldUser.getId());
        newUser.setName("Петр Петров");
        newUser.setEmail("petr@retrov.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        User actualUser = userService.update(newUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("Петр Петров", savedUser.getName());
        assertEquals("petr@retrov.ru", savedUser.getEmail());
    }

    @Test
    void getUserById_whenUserFound_thenReturnedUser() {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUsers = userService.getUserById(userId);

        assertEquals(expectedUser, actualUsers);
    }

    @Test
    void getUserById_whenUserNotFound_thenUserNotFoundException() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }
}
