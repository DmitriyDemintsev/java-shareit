package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.dto.ItemDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto implements Serializable {
    private Long id; // идентификатор запроса
    private String description; //описание для запрашиваемой вещи
    private LocalDateTime created; //дата и время создания запроса
    private List<ItemDto> items;
}
