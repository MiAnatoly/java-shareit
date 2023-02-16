package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;

import java.util.List;

public interface RequestService {

    ItemRequestRefundDto add(Long userId, ItemRequestDto requestDto);

    List<ItemRequestRefundDto> findAllOwner(Long userId);

    List<ItemRequestRefundDto> findAllNotOwner(Long userId, Integer page, Integer size);

    ItemRequestRefundDto findById(Long userId, Long requestId);
}
