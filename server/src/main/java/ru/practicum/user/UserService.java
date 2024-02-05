package ru.practicum.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.model.User;

import java.util.List;

@Transactional(readOnly = true)
public
interface UserService {
    List<User> getAllUsers();

    @Transactional
    User create(User user);

    @Transactional
    User update(User user);

    @Transactional
    void deleteUser(long id);

    User getUserById(long id);
}
