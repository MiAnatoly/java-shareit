package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item add(long userId, Item item);

    Item edit(long userId, long itemId, Item item);

    Item findById(long itemId);

    List<Item> findAll(long userId);

    List<Item> search(String text);
}
