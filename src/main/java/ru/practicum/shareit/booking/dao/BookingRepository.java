package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdAndStatus(Long bookerId, Status status);

    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    @Query("select b from Booking b where b.item.owner.id = ?1 order by b.start desc")
    List<Booking> findAllOwner(Long userId);

    @Query("select b from Booking b where b.item.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end order by b.start desc")
    List<Booking> findCurrentOwner(Long itemId);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < current_timestamp order by b.start desc")
    List<Booking> findPastOwner(Long itemId);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start > current_timestamp order by b.start desc")
    List<Booking> findFutureOwner(Long itemId);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 order by b.start desc")
    List<Booking> findStatusOwner(Long itemId, Status status);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.item.id = ?2" +
            " and b.status = ?3")
    List<Booking> findItemByOwner(Long userId, Long itemId, Status status);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllItemsByOwner(Long userId, Status status);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2" +
            " and b.status = ?3 and b.start < current_timestamp ")
    List<Booking> findByBooker_IdAndItem_Id(Long userId, Long itemId, Status status);
}
