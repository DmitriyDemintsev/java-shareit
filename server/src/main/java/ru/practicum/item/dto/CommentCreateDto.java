package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CommentCreateDto implements Serializable {
    private Long id; //идентификатор комментария
    private String text; //содержимое комментария
}
