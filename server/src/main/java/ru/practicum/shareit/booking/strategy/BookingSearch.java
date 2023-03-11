package ru.practicum.shareit.booking.strategy;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingSearch {

    List<Booking> findAllForUser(BookingParam bookingParam);

    List<Booking> findAllForOwner(BookingParam bookingParam);

    State getState();

}
