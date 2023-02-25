package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static Booking toBooking(User user, Item item, BookingDto bookingDto) {
        return new Booking(
                null,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user,
                null
        );
    }

    public static BookingItemDto toBookingItemDto(Booking booking) {
        return new BookingItemDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new BookingItemDto.Booker(booking.getBooker().getId(), booking.getBooker().getName()),
                new BookingItemDto.Item(booking.getItem().getId(), booking.getItem().getName())
        );
    }

    public static List<BookingItemDto> toBookingsItemDto(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toBookingItemDto).collect(Collectors.toList());
    }
}
