package ru.practicum.shareit.comment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItem_Id(Long itemId);

    List<Comment> findByItemInOrderByCreatedDesc(List<Item> item);
}
