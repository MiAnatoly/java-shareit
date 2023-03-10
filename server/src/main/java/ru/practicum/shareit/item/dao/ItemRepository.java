package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where i.owner = ?1 " +
            "order by i.id")
    Page<Item> findAllForUser(User user, Pageable pageable);

    @Query("select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) and i.isAvailable = true")
    Page<Item> search(String text, Pageable pageable);

    @Query("select i from Item i where i.request in ?1 and i.isAvailable = true")
    List<Item> findByRequesters(List<ItemRequest> requests);

    @Query("select i from Item i where i.request = ?1")
    List<Item> findByRequester(ItemRequest requests);
}
