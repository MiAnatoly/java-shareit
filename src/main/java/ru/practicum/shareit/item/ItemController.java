package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRefundDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.booking.valide.ValidPage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemRefundDto add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody ItemDto itemDto
    ) {
        return service.add(userId, itemDto);
    }
    // добавить вещь


    @PatchMapping("/{itemId}")
    public ItemRefundDto edit(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @Validated({Update.class}) @RequestBody ItemDto itemDto
    ) {
        return service.edit(userId, itemId, itemDto);
    }
    // внести изменения в данные о вещи

    @GetMapping("/{itemId}")
    public ItemBookingDto findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId
    ) {
        return service.findById(userId, itemId);
    }
    // показать вещь по id

    @GetMapping
    public List<ItemBookingDto> findAll(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        int page = ValidPage.page(from, size);
        return service.findAllForUser(page, size, userId);
    }
    // показать все добавленные вещи пользователя

    @GetMapping("/search")
    public List<ItemRefundDto> search(
            @RequestParam String text,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        if (text.isBlank()) {
            return List.of();
        }
        int page = ValidPage.page(from, size);
        return service.search(text, page, size);
    }
    // найти все доступные вещи

    @PostMapping("/{itemId}/comment")
    public CommentRefundDto addComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody CommentDto commentDto,
            @PathVariable Long itemId
    ) {
        return service.addComment(userId, itemId, commentDto);
    }
    // добавить коментарий
}
