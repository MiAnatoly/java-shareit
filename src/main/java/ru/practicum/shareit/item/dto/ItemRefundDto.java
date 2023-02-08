package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemRefundDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}

