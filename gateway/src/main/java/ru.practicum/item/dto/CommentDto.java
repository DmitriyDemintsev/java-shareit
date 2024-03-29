package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
}
