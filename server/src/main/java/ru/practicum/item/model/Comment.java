package ru.practicum.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //идентификатор комментария
    @Column(nullable = false)
    private String text; //содержимое комментария
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item; //вещь, к которой относится комментарий
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author; //автор комментария - тот, кто пользовался вещью
    @Column(nullable = false)
    private LocalDateTime created; //дата создания комментария
}
