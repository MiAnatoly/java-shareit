package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENTS", schema = "PUBLIC")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DESCRIPTION")
    private String text;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private User author;
    @Column(name = "CREATED")
    private LocalDateTime created;

}
