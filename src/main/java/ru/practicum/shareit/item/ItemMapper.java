package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentRefundDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(
                null,
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null
        );
    }

    public static ItemRefundDto toItemRefundDto(Item item) {
        return new ItemRefundDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable()
        );
    }

    public static List<ItemRefundDto> toItemsRefundDto(List<Item> items) {
        return items.stream().map(ItemMapper::toItemRefundDto).collect(Collectors.toList());
    }

    public static ItemBookingDto toItemBookingDto(Booking last, Booking next, List<Comment> comments, Item item) {
        List<CommentRefundDto> commentsDto = CommentMapper.toCommentsDto(comments);
        return new ItemBookingDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                findByBooking(last),
                findByBooking(next),
                commentsDto
        );
    }

    private static ItemBookingDto.Booking findByBooking(Booking booking) {
        if (booking != null) {
            return new ItemBookingDto.Booking(booking.getId(), booking.getBooker().getId());
        } else {
            return null;
        }
    }

}
