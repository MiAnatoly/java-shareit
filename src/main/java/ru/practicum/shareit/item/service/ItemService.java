package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto add(long userId, ItemDto itemDto);

    ItemDto edit(long userId, long itemId, ItemDto itemDto);

    ItemBookingDto findById(long userId, long itemId);

    List<ItemBookingDto> findAllForUser(long userId);

    List<ItemDto> search(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
