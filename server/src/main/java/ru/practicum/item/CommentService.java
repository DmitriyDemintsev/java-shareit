package ru.practicum.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.model.Comment;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentService {
    @Transactional
    Comment create(Long userId, Long itemId, Comment comment);

    List<Comment> getComments(Long itemId);
}
