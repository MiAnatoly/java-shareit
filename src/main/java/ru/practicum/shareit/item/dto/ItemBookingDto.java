package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.comment.dto.CommentRefundDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentRefundDto> comments;

    @Data
    public static class Booking {
        private final Long id;
        private final Long bookerId;
    }
}
