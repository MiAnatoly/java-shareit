package ru.practicum.shareit.booking.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrentBookingSearch implements BookingSearch {

    private final BookingRepository bookingDao;

    @Override
    public List<Booking> findAllForUser(BookingParam bookingParam) {
        return bookingDao.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                bookingParam.getUserId(), LocalDateTime.now(), LocalDateTime.now(),
                bookingParam.getPage()).getContent();
    }

    @Override
    public List<Booking> findAllForOwner(BookingParam bookingParam) {
        return bookingDao.findCurrentOwner(bookingParam.getUserId(), bookingParam.getPage()).getContent();
    }

    @Override
    public State getState() {
        return State.CURRENT;
    }
}
