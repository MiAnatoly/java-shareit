package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    BookingItemDto add(Long userId, BookingDto bookingDto);

    BookingItemDto approved(Long userId, Long bookingId, Boolean approved);

    BookingItemDto findById(Long userId, Long bookingId);

    List<BookingItemDto> findAllForUser(Long userId, State state);

    List<BookingItemDto> findAllForOwner(Long userId, State state);
}
