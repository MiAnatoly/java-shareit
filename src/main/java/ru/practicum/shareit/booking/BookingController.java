package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated({Create.class}) @RequestBody BookingDto bookingDto) {
            return service.add(userId, bookingDto);
    }

    // добавить запрос
    @PatchMapping("/{bookingId}")
    public BookingItemDto approved(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return service.approved(userId, bookingId, approved);
    }
    // подтверждение или отклонения запроса

    @GetMapping("/{bookingId}")
    public BookingItemDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return service.findById(userId, bookingId);
    }

    // показать запрос пользователя владельцу запроса или вещи
    @GetMapping()
    public List<BookingItemDto> findAllForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String state) {
        State bookingState = validate(state);
        return service.findAllForUser(userId, bookingState);
    }

    // показать все запросы владельцу запросов (разбивка по статусам)
    @GetMapping("/owner")
    public List<BookingItemDto> findAllForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String state) {
        State bookingState = validate(state);
        return service.findAllForOwner(userId, bookingState);
    }

    // показать все запросы владельцу вещей (разбивка по статусам)
    private State validate(String state) {
        State bookingState = null;
        for (State value : State.values()) {
            if (value.name().equals(state)) {
                bookingState = value;
            }
        }
        if (bookingState == null) {
            throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingState;
    }
}
