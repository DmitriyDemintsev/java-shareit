package ru.practicum.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking save(Booking booking); //создать/обновить

    void deleteById(long id); //удалить

    List<Booking> findAll(); //показать все бронирования

    Booking getById(long id); //найти бронирование по его id

    Page<Booking> findByBookerAndStatus(User booker, BookingStatus status, Pageable pageable); //бронирования по арендатору и статусу

    Page<Booking> findByBooker(User booker, Pageable pageable); //бронирования по арендатору

    Page<Booking> findByBookerAndEndIsBefore(User booker, LocalDateTime end, Pageable pageable); //завершенные заказы

    List<Booking> findByBookerAndEndIsBeforeAndItem(User booker, LocalDateTime end, Item item); //все завершенные заказы для вещи и заказчика

    Page<Booking> findByBookerAndStartIsAfter(User booker, LocalDateTime start, Pageable pageable); //будущие заказы

    List<Booking> findByItemAndStartIsBeforeAndStatus(Item item, LocalDateTime start, BookingStatus status, Sort sort); //заказы по времени окончания

    List<Booking> findByItemAndStartIsAfterAndStatus(Item item, LocalDateTime end, BookingStatus status, Sort sort); //заказы по времени окончания

    Page<Booking> findByBookerAndStartIsBeforeAndEndIsAfter(User booker, LocalDateTime start,
                                                            LocalDateTime end, Pageable pageable); //текущие заказы

    @Query("select b from Booking b join b.item as i " +
            "where i.owner = ?1 and b.status = ?2")
    Page<Booking> getBookingsAllItemsForUserWithStatus(User owner, BookingStatus status, Pageable pageable); //все заказы со стасутом

    @Query("select b from Booking b join b.item as i " +
            "where i.owner = ?1")
    Page<Booking> getBookingsAllItemsForUser(User owner, Pageable pageable); //вообще все заказы

    @Query("select b from Booking b join b.item as i " +
            "where i.owner = ?1 and b.end < ?2")
    Page<Booking> findCompletedBookings(User booker, LocalDateTime end, Pageable pageable); //завершенные заказы

    @Query("select b from Booking b join b.item as i " +
            "where i.owner = ?1 and b.start > ?2")
    Page<Booking> findFutureBookings(User booker, LocalDateTime start, Pageable pageable); //будущие заказы

    @Query("select b from Booking b join b.item as i " +
            "where i.owner = ?1 and b.start < ?2 and b.end > ?3")
    Page<Booking> findCurrentBookings(User booker, LocalDateTime start, LocalDateTime end, Pageable pageable); //текущие заказы
}
