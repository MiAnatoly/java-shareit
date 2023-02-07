package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @NotNull(groups = {Create.class})
    private Long itemId;
    private Long bookerId;
    private Status status;
}
