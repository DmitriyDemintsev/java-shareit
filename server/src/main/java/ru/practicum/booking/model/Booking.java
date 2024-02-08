package ru.practicum.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime start; //дата и время начала бронирование
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end; //дата и время окончания бронирования
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item; //что бронируют
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker; //кто бронирует
    @Column(nullable = false)
    private BookingStatus status; //статус бронирования
}
