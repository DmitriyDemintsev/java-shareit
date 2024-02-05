package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.UserValidationException;
import ru.practicum.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    @Transactional
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new UserValidationException("Отсутствует имя пользователя / логин");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserValidationException("Не указан email");
        }
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        User old = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("user не найден"));
        if (user.getName() == null) {
            user.setName(old.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(old.getEmail());
        }
        if (user.getName().isEmpty()) {
            throw new UserValidationException("Отсутствует имя/логин пользователя");
        }
        if (user.getEmail().isEmpty()) {
            throw new UserValidationException("Отсутствует email");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user не найден"));
    }
}
