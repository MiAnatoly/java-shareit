package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentRefundDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
