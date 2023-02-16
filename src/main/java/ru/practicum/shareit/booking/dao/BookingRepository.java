package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBooker_IdAndStatus(Long bookerId, Status status, Pageable pageable);

    Page<Booking> findByBooker_IdOrderByStartDesc(Long bookerId, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b where b.item.owner = ?1 order by b.start desc")
    Page<Booking> findAllOwner(User user, Pageable pageable);

    @Query("select b from Booking b where b.item.owner = ?1 " +
            "and current_timestamp between b.start and b.end order by b.start desc")
    Page<Booking> findCurrentOwner(User user, Pageable pageable);

    @Query("select b from Booking b where b.item.owner = ?1 and b.end < current_timestamp order by b.start desc")
    Page<Booking> findPastOwner(User user, Pageable pageable);

    @Query("select b from Booking b where b.item.owner = ?1 and b.start > current_timestamp order by b.start desc")
    Page<Booking> findFutureOwner(User user, Pageable pageable);

    @Query("select b from Booking b where b.item.owner = ?1 and b.status = ?2 order by b.start desc")
    Page<Booking> findStatusOwner(User user, Status status, Pageable pageable);

    @Query("select b from Booking b where b.item.owner = ?1 and b.item.id = ?2" +
            " and b.status = ?3")
    List<Booking> findItemByOwner(User user, Long itemId, Status status);

    @Query("select b from Booking b where b.item.owner = ?1 and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllItemsByOwner(User user, Status status);

    @Query("select b from Booking b where b.booker = ?1 and b.item.id = ?2" +
            " and b.status = ?3 and b.start < current_timestamp ")
    List<Booking> findByBooker_IdAndItem_Id(User user, Long itemId, Status status);
}
