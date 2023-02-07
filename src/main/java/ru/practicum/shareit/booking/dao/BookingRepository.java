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

    @Query("select b from Booking b where b.item.Owner.id = ?1 order by b.start desc")
    List<Booking> findAllOwner(Long userId);

    @Query("select b from Booking b where b.item.Owner.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start desc")
    List<Booking> findCurrentOwner(Long itemId, LocalDateTime dateTime);

    @Query("select b from Booking b where b.item.Owner.id = ?1 and b.end < ?2 order by b.start desc")
    List<Booking> findPastOwner(Long itemId, LocalDateTime dateTime);

    @Query("select b from Booking b where b.item.Owner.id = ?1 and b.start > ?2 order by b.start desc")
    List<Booking> findFutureOwner(Long itemId, LocalDateTime dateTime);

    @Query("select b from Booking b where b.item.Owner.id = ?1 and b.status = ?2 order by b.start desc")
    List<Booking> findStatusOwner(Long itemId, Status status);

    @Query(nativeQuery = true, value = "select b.* from BOOKINGS as b" +
            " join ITEMS I on I.ID = b.ITEM_ID " +
            "where i.OWNER_ID = ?1 and b.ITEM_ID = ?2 order by b.START_DATE desc limit 1")
    Booking lastBooking(Long userId, Long itemId);

    @Query(nativeQuery = true, value = "select b.* from BOOKINGS as b " +
            " join ITEMS I on I.ID = b.ITEM_ID " +
            "where i.OWNER_ID = ?1 and b.ITEM_ID = ?2 " +
            "order by b.START_DATE limit 1")
    Booking nextBooking(Long userId, Long itemId);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2 and b.status = ?3 and b.start < ?4")
    List<Booking> findByBooker_IdAndItem_Id(Long userId, Long itemId, Status status, LocalDateTime date);
}
