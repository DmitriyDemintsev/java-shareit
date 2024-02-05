package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save (User user); //создать/обновить

    List<User> findAll(); //найти всех user

    User getById(long id); //получить user по id

    void deleteById(long id); //удалить user

    Optional<User> findByEmail(String email); //найти user по email
}
