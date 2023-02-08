package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRefundDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;

import java.util.List;

public interface ItemService {

    ItemRefundDto add(long userId, ItemDto itemDto);

    ItemRefundDto edit(long userId, long itemId, ItemDto itemDto);

    ItemBookingDto findById(long userId, long itemId);

    List<ItemBookingDto> findAllForUser(long userId);

    List<ItemRefundDto> search(String text);

    CommentRefundDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
