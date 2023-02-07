package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null
        );
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable()
        );
    }

    public static ItemBookingDto toItemBookingDto(Booking last, Booking next, List<Comment> comments, Item item) {
        BookingDto lastDto = null;
        BookingDto nextDto = null;
        List<CommentDto> commentsDto = CommentMapper.toCommentsDto(comments);
        if (last != null) {
            lastDto = BookingMapper.toBookingDto(last);
        }
        if (next != null) {
            nextDto = BookingMapper.toBookingDto(next);
        }
        return new ItemBookingDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                lastDto,
                nextDto,
                commentsDto
        );
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

}
