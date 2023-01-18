package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto add(long userId, ItemDto itemDto);

    ItemDto edit(long userId, long itemId, ItemDto itemDto);

    ItemDto findById(long itemId);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(String text);
}
