package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.CommentCreateDto;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.CommentMapper;

import javax.validation.Valid;

@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable("itemId") long itemId,
                                    @Valid @RequestBody CommentCreateDto commentCreateDto) {

        return CommentMapper.toCommentDto(commentService.create(userId, itemId,
                CommentMapper.toComment(commentCreateDto, null)), true);
    }
}
