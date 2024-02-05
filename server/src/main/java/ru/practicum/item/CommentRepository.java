package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment save (Comment comment);

    List<Comment> findAllByItem(Item item);
}
