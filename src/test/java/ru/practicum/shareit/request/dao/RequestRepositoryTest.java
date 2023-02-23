package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    User user1 = new User(null, "142@mail.ru", "Bob1");
    User user2 = new User(null, "242@mail.ru", "Bob2");
    ItemRequest request1 = new ItemRequest(null, "нужен зонт1", user1, LocalDateTime.now());
    ItemRequest request2 = new ItemRequest(null, "нужен зонт2", user2, LocalDateTime.now());

    @BeforeEach
    void addRequests() {
        userRepository.save(user1);
        userRepository.save(user2);
        requestRepository.save(request1);
        requestRepository.save(request2);
    }

    @Test
    void findByRequester() {
        List<ItemRequest> requests = requestRepository.findByRequester(user1);
        ItemRequest request = requests.get(0);

        assertEquals(requests.size(), 1);
        assertEquals(request.getDescription(), "нужен зонт1");
        assertEquals(request.getRequester().getName(), "Bob1");
    }

    @Test
    void findByNotRequester() {
        List<ItemRequest> requests = requestRepository.findByNotRequester(user1, Pageable.ofSize(4))
                .getContent();
        ItemRequest request = requests.get(0);
        assertEquals(requests.size(), 1);
        assertEquals(request.getDescription(), "нужен зонт2");
        assertEquals(request.getRequester().getName(), "Bob2");
    }
}