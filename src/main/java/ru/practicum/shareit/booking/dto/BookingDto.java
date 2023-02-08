package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.valide.DateStartBeforeEnd;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@DateStartBeforeEnd(groups = {Create.class})
public class BookingDto {
    private Long id;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    @Future(groups = {Create.class})
    private LocalDateTime end;
    @NotNull(groups = {Create.class})
    private Long itemId;
    private Long bookerId;
    private Status status;
}
