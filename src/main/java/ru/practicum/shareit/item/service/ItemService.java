package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item add(long userId, Item item);

    Item edit(long userId, long itemId, Item item);

    Item findById(long itemId);

    List<Item> findAll(long userId);

    List<Item> search(String text);
}
