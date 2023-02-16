package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select i from ItemRequest i where i.requester = ?1 order by i.created desc")
    List<ItemRequest> findByRequester(User user);

    @Query("select i from ItemRequest i where i.requester not in ?1 order by i.created desc")
    Page<ItemRequest> findByNotRequester(User user, Pageable pageable);
}
