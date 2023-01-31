package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Setter
@Getter
@AllArgsConstructor
public class Item {
    private Long id;
    private User owner;
    private String name;
    private String description;
    private Boolean isAvailable;
}
