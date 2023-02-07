package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank(groups = {Create.class})
    @Size(max = 200, groups = {Update.class, Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private User requester;
}
