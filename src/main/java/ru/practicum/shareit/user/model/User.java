package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class User {
    private long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
