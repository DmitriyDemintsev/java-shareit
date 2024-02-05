package ru.practicum.reviews.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@SuperBuilder
public class Reviews {
    private Long reviewId;
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    private Long userId;
    private Long itemId;
    private Integer useful = 0;
}
