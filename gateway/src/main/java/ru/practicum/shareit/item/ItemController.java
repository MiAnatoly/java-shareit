package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.booking.valide.ValidPage;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody ItemDto itemDto
    ) {
        log.info("Post item with userId={}", userId);
        return itemClient.add(userId, itemDto);
    }
    // добавить вещь


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> edit(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @Validated({Update.class}) @RequestBody ItemDto itemDto
    ) {
        log.info("Patch item with userId={}, itemId={}", userId, itemId);
        return itemClient.edit(userId, itemId, itemDto);
    }
    // внести изменения в данные о вещи

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId
    ) {
        log.info("Get item with userId={}, itemId={}", userId, itemId);
        return itemClient.findById(userId, itemId);
    }
    // показать вещь по id

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get item with userId={}", userId);
        return itemClient.findAllForUser(page, size, userId);
    }
    // показать все добавленные вещи пользователя

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam String text,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        int page = ValidPage.page(from, size);
        log.info("Get item search={}", text);
        return itemClient.search(text, page, size);
    }
    // найти все доступные вещи

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody CommentDto commentDto,
            @PathVariable Long itemId
    ) {
        log.info("Post comment itemId={}", itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
    // добавить коментарий
}
