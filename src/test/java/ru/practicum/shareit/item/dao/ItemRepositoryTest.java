package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    User user1 = new User(null, "142@mail.ru", "Bob1");
    User user2 = new User(null, "242@mail.ru", "Bob2");
    User user3 = new User(null, "342@mail.ru", "Bob3");
    User user4 = new User(null, "442@mail.ru", "Bob4");
    ItemRequest request1 = new ItemRequest(null, "нужен зонт1", user1, LocalDateTime.now());
    ItemRequest request2 = new ItemRequest(null, "нужен зонт2", user2, LocalDateTime.now().minusDays(1));
    Item item1 = new Item(null, user1, "зонт1", "от дождя1", true, request1);
    Item item2 = new Item(null, user2, "зонт2", "от дождя2", true, request2);
    Item item3 = new Item(null, user3, "зонт3", "от дождя3", false, null);

    @BeforeEach
    void addItem() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        requestRepository.save(request1);
        requestRepository.save(request2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
    }

    @Test
    void findAllForUser() {
        List<Item> items = itemRepository.findAllForUser(user1, PageRequest.ofSize(5)).getContent();
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 1);
        assertEquals(item.getDescription(), "от дождя1");
        assertEquals(item.getOwner().getName(), "Bob1");

    }

    @Test
    void search() {
        String text = "От дождя1";
        List<Item> items = itemRepository.search(text, PageRequest.ofSize(5)).getContent();
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 1);
        assertEquals(item.getDescription(), "от дождя1");
        assertEquals(item.getOwner().getName(), "Bob1");
    }

    @Test
    void findByRequesters() {
        List<ItemRequest> requests = List.of(request1, request2);
        List<Item> items = itemRepository.findByRequesters(requests);
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 2);
        assertEquals(item.getDescription(), "от дождя1");
        assertEquals(item.getOwner().getName(), "Bob1");
    }

    @Test
    void findByRequester() {
        List<Item> items = itemRepository.findByRequester(request1);
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 1);
        assertEquals(item.getDescription(), "от дождя1");
        assertEquals(item.getOwner().getName(), "Bob1");
    }
}