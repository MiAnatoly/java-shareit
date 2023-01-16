package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.servece.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDto itemDto;
    private final UserService userService;

    @Override
    public Item add(long userId, Item item) {
        userService.findById(userId);
        return itemDto.add(userId, item);
    }

    @Override
    public Item edit(long userId, long itemId, Item item) {
        userService.findById(userId);
        return itemDto.edit(userId, itemId, item);
    }

    @Override
    public Item findById(long itemId) {
        return itemDto.findById(itemId);
    }

    @Override
    public List<Item> findAll(long userId) {
        return itemDto.findAll(userId);
    }

    @Override
    public List<Item> search(String text) {
        return itemDto.search(text);
    }
}
