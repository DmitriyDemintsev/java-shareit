package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.item.model.Comment;
import ru.practicum.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CommentMapper {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Comment toComment(CommentCreateDto commentCreateDto, Long id) {
        return new Comment(
                id,
                commentCreateDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                itemMapper.toItemDto(comment.getItem()),
                userMapper.toUserDto(comment.getAuthor()),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public List<CommentDto> toItemDtoListList(Iterable<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(toCommentDto(comment));
        }
        return result;
    }
}
