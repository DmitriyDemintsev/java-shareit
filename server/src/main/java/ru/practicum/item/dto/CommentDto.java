package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id; // идентификатор комментария
    private String text; //содержимое комментария
    private ItemDto item; //вещь, к которой относится комментарий
    private UserDto author; //автор комментария - тот, кто пользовался вещью
    private String authorName;
    private LocalDateTime created; //дата создания комментария
}
