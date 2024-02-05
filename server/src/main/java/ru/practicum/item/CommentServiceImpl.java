package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.BookingRepository;
import ru.practicum.exception.BookingValidationException;
import ru.practicum.exception.CommentValidationException;
import ru.practicum.exception.ItemValidationException;
import ru.practicum.exception.UserValidationException;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Comment create(Long userId, Long itemId, Comment comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Отзыв может оставить только арендатор"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemValidationException("Отзыв на данный item вам недоступен"));
        LocalDateTime now = LocalDateTime.now();
        comment.setAuthor(user);
        comment.setItem(item);
        if (bookingRepository.findByBookerAndEndIsBeforeAndItem(comment.getAuthor(), now, comment.getItem()).isEmpty()) {
            throw new BookingValidationException("Нет завершенных заказов");
        }
        if (comment.getText() == null || comment.getText().isEmpty()) {
            throw new CommentValidationException("Комментарий не может быть пустым");
        }
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemValidationException("Отзыв на данный item вам недоступен"));
        return commentRepository.findAllByItem(item);
    }
}
