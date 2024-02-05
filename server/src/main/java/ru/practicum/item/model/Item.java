package ru.practicum.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //идентификатор вещи
    @Column(nullable = false)
    private String name; //название вещи
    @Column(nullable = false)
    private String description; //описание вещи
    private Boolean available; //доступность вещи для аренды
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner; //владелец вещи (тот, кто создал её)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request; //ссылка на запрос user'а, для которого была создана вещь == ответ на запрос
}
