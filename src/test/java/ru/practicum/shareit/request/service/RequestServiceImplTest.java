package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    RequestRepository requestDao;
    @Mock
    UserRepository userDao;
    @Mock
    ItemRepository itemDao;

    @InjectMocks
    RequestServiceImpl service;

    // Метод add
    @Test
    void add_whenUserFound_thenReturnRequest() {
        Long userId = 1L;
        User user = new User(userId, "234@mail.ru", "Bob");
        ItemRequestDto itemRequestDto = new ItemRequestDto("нужен рубанок");
        ItemRequest request = new ItemRequest(
                null,
                "нужен рубанок",
                user,
                LocalDateTime.now()
                );

        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(requestDao.save(any())).thenReturn(request);

        ItemRequestRefundDto itemRequestRefundDto = service.add(userId, itemRequestDto);

        assertNull(itemRequestRefundDto.getId());
        assertEquals(itemRequestRefundDto.getDescription(), "нужен рубанок");

    }

    @Test
    void add_whenUserNotFound_thenReturnNotObjectException() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto("нужен рубанок");

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.add(userId, itemRequestDto), "нет пользователя");

        verify(requestDao, never()).findById(userId);
    }

    // Метод findAllOwner
    @Test
    void findAllOwner_whenUserFound_thenReturnRequests() {
        Long userId = 1L;
        User user = new User(userId, "234@mail.ru", "Bob");
        List<ItemRequest> requests = new ArrayList<>();
        ItemRequest request1 = new ItemRequest(
                1L,
                "нужен рубанок",
                user,
                LocalDateTime.now()
        );
        ItemRequest request2 = new ItemRequest(
                2L,
                "нужны лыжные палки",
                user,
                LocalDateTime.now()
        );
        requests.add(request1);
        requests.add(request2);

        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(requestDao.findByRequester(user)).thenReturn(requests);

        List<ItemRequestRefundDto> requestsRefundDto = service.findAllOwner(userId);

        assertEquals(requestsRefundDto.size(), 2);
    }

    @Test
    void findAllOwner_whenUserNotFound_thenReturnNotObjectException() {
        Long userId = 1L;

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.findAllOwner(userId), "нет пользователя");

        verify(requestDao, never()).findByRequester(any());
    }

    // Метод findAllNotOwner
    @Test
    void findAllNotOwner_whenUserFound_thenReturnRequests() {
        long userId = 1L;
        int page = 0;
        int size = 5;
        User user = new User(userId, "234@mail.ru", "Bob");
        List<ItemRequest> requests = new ArrayList<>();
        ItemRequest request1 = new ItemRequest(
                1L,
                "нужен рубанок",
                user,
                LocalDateTime.now()
        );
        ItemRequest request2 = new ItemRequest(
                2L,
                "нужны лыжные палки",
                user,
                LocalDateTime.now().plusHours(2)
        );
        requests.add(request1);
        requests.add(request2);
        PageImpl<ItemRequest> requestsPage = new PageImpl<>(requests);

        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(requestDao.findByNotRequester(any(), any())).thenReturn(requestsPage);

        List<ItemRequestRefundDto> requestsRefundDto = service.findAllNotOwner(userId, page, size);

        assertEquals(requestsRefundDto.size(), 2);
    }

    @Test
    void findAllNotOwner_whenUserNotFound_thenReturnNotObjectException() {
        long userId = 1L;
        int page = 0;
        int size = 5;

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.findAllNotOwner(userId, page, size),
                "нет пользователя");

        verify(requestDao, never()).findByNotRequester(any(), any());
    }

    // Метод findById
    @Test
    void findById_whenUserAndItemFound_thenReturnRequest() {
        Long userId = 1L;
        Long requestId = 1L;
        User user1 = new User(userId, "234@mail.ru", "Bob");
        User user2 = new User(2L, "2324@mail.ru", "BobRay");
        ItemRequest request = new ItemRequest(
                requestId,
                "нужен рубанок",
                user1,
                LocalDateTime.now()
        );
        List<Item> items = new ArrayList<>();
        Item item = new Item(
                1L,
                user2,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );
        items.add(item);

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(requestDao.findById(userId)).thenReturn(Optional.of(request));
        when(itemDao.findByRequester(request)).thenReturn(items);

        ItemRequestRefundDto requestRefundDto = service.findById(userId, requestId);

        assertEquals(requestRefundDto.getId(), 1);
        assertEquals(requestRefundDto.getItems().size(), 1);
        assertEquals(requestRefundDto.getDescription(), "нужен рубанок");
    }

    @Test
    void findById_whenRequestNotFound_thenReturnNotObjectException() {
        Long userId = 1L;
        Long requestId = 1L;
        User user1 = new User(userId, "234@mail.ru", "Bob");

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(requestDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.findById(userId, requestId),
                "нет запроса");

        verify(itemDao, never()).findByRequesters(any());
    }

    @Test
    void findById_whenUserNotFound_thenReturnNotObjectException() {
        Long userId = 1L;
        Long requestId = 1L;

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.findById(userId, requestId),
                "нет пользователя");

        verify(requestDao, never()).findById(anyLong());
        verify(itemDao, never()).findByRequesters(any());
    }
}