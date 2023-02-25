package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRefundDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemRefundDto add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemDto itemDto
    ) {
        log.info("Post item with userId={}", userId);
        return service.add(userId, itemDto);
    }
    // добавить вещь


    @PatchMapping("/{itemId}")
    public ItemRefundDto edit(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody ItemDto itemDto
    ) {
        log.info("Patch item with userId={}, itemId={}", userId, itemId);
        return service.edit(userId, itemId, itemDto);
    }
    // внести изменения в данные о вещи

    @GetMapping("/{itemId}")
    public ItemBookingDto findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId
    ) {
        log.info("Get item with userId={}, itemId={}", userId, itemId);
        return service.findById(userId, itemId);
    }
    // показать вещь по id

    @GetMapping
    public List<ItemBookingDto> findAll(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("Get item with userId={}", userId);
        return service.findAllForUser(from, size, userId);
    }
    // показать все добавленные вещи пользователя

    @GetMapping("/search")
    public List<ItemRefundDto> search(
            @RequestParam String text,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("Get item search={}", text);
        return service.search(text, from, size);
    }
    // найти все доступные вещи

    @PostMapping("/{itemId}/comment")
    public CommentRefundDto addComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody CommentDto commentDto,
            @PathVariable Long itemId
    ) {
        log.info("Post comment itemId={}", itemId);
        return service.addComment(userId, itemId, commentDto);
    }
    // добавить коментарий
}
