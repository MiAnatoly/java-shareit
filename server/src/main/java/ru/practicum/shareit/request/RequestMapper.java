package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static ItemRequest toRequest(User user, ItemRequestDto requestDto) {
        return new ItemRequest(
                null,
                requestDto.getDescription(),
                user,
                LocalDateTime.now()
        );
    }

    public static ItemRequestRefundDto toRequestRefundDto(ItemRequest request, List<Item> items) {
        List<ItemRequestRefundDto.Item> itemsDto = items
                .stream()
                .map(RequestMapper::toRefundDto)
                .collect(Collectors.toList());
        return new ItemRequestRefundDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                itemsDto
        );
    }

    private static ItemRequestRefundDto.Item toRefundDto(Item item) {
        return new ItemRequestRefundDto.Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                item.getRequest().getId()
                );
    }
}
