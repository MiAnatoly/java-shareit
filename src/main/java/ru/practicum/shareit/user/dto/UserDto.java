package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private String email;
    @NotBlank(groups = {Create.class})
    private String name;
}
