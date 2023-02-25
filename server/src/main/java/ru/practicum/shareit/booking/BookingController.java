package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingItemDto add(
            @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody BookingDto bookingDto
    ) {
        log.info("Post booking with userId={}", userId);
        return service.add(userId, bookingDto);
    }
    // добавить запрос

    @PatchMapping("/{bookingId}")
    public BookingItemDto approved(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        log.info("Patch booking with bookingId {}, userId={}, approved={}", bookingId, userId, approved);
        return service.approved(userId, bookingId, approved);
    }
    // подтверждение или отклонения запроса

    @GetMapping("/{bookingId}")
    public BookingItemDto findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId
    ) {
        log.info("Get booking with userId={}, bookingId={}", userId, bookingId);
        return service.findById(userId, bookingId);
    } // показать запрос пользователя владельцу запроса или вещи

    @GetMapping()
    public List<BookingItemDto> findAllForUser(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam String state,
            @RequestParam int from,
            @RequestParam int size
    ) {
        State bookingState = State.valueOf(state);
        log.info("Get booking with userId={}", userId);
        return service.findAllForUser(userId, bookingState, from, size);
    }

    // показать все запросы владельцу запросов (разбивка по статусам)
    @GetMapping("/owner")
    public List<BookingItemDto> findAllForOwner(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam String state,
            @RequestParam int from,
            @RequestParam int size
    ) {
        State bookingState = State.valueOf(state);
        log.info("Get booking with ownerId={}", userId);
        return service.findAllForOwner(userId, bookingState, from, size);
    }
    // показать все запросы владельцу вещей (разбивка по статусам)

}
