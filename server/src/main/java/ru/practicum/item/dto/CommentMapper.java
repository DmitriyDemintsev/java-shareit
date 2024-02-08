package ru.practicum.item.dto;

import ru.practicum.item.model.Comment;
import ru.practicum.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static Comment toComment(CommentCreateDto commentCreateDto, Long id) {
        return new Comment(
                id,
                commentCreateDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment, boolean isIncludeItem) {
        if (isIncludeItem == true) {
            return new CommentDto(
                    comment.getId(),
                    comment.getText(),
                    ItemMapper.toItemDto(comment.getItem()),
                    UserMapper.toUserDto(comment.getAuthor()),
                    comment.getAuthor().getName(),
                    comment.getCreated());
        }
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                null,
                UserMapper.toUserDto(comment.getAuthor()),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static List<CommentDto> toCommentDtoListList(Iterable<Comment> comments, boolean isIncludeItem) {
        List<CommentDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(toCommentDto(comment, isIncludeItem));
        }
        return result;
    }
}
