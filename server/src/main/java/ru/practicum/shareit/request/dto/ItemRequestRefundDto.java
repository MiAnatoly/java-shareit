package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestRefundDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<Item> items;

    @Data
    public static class Item {
        private final Long id;
        private final String name;
        private final String description;
        private final Boolean available;
        private final Long requestId;
    }
}


