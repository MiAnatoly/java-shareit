package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody Item item) {
        return itemService.add(userId, item);
    }
    // добавить вещь

    @PatchMapping("/{itemId}")
    public Item edit(@RequestHeader("X-Sharer-User-Id") long userId,
                     @PathVariable long itemId, @RequestBody Item item) {
        return itemService.edit(userId, itemId, item);
    }
    // внести изменения в данные о вещи

    @GetMapping("/{itemId}")
    public Item findById(@PathVariable long itemId) {
        return itemService.findById(itemId);
    }
    // показать вещь по id

    @GetMapping
    public List<Item> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAll(userId);
    }
    // показать все добавленные вещи пользователя

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        return itemService.search(text);
    }
    // найти все доступные вещи
}
