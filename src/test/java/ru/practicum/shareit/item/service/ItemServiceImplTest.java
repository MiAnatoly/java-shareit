package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRefundDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemRepository itemDao;
    @Mock
    UserRepository userDao;
    @Mock
    CommentRepository commentDao;
    @Mock
    BookingRepository bookingDao;
    @Mock
    RequestRepository requestDao;
    @InjectMocks
    ItemServiceImpl itemService;

    // метод add
    @Test
    void add_whenUserFound_AndRequestNotFound_thenReturnItemWithoutRequest() {
        long id = 1L;
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                null
        );
        User user = new User(id, "23@mail.ru", "Jon");
        Item item = new Item(
                1L,
                user,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                null
        );

        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemDao.save(any())).thenReturn(item);

        ItemRefundDto itemRefundDto = itemService.add(id, itemDto);

        assertEquals(itemRefundDto.getId(), 1L);
        assertEquals(itemRefundDto.getName(), "Ручной рубанок");
        assertEquals(itemRefundDto.getDescription(),
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм");
        assertNull(itemRefundDto.getRequestId());

        verify(userDao).findById(anyLong());
        verify(requestDao, never()).findById(anyLong());
        verify(itemDao).save(any());
    }

    @Test
    void add_whenUserFound_AndRequestFound_thenReturnItemWithRequest() {
        long id = 1L;
        User user = new User(id, "23@mail.ru", "Jon");
        ItemRequest request = new ItemRequest(
                id,
                "нужен рубанок по дереву",
                user,
                LocalDateTime.now().minusDays(1)
        );
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                id
        );
        Item item = new Item(
                1L,
                user,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );

        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemDao.save(any())).thenReturn(item);
        when(requestDao.findById(anyLong())).thenReturn(Optional.of(request));

        ItemRefundDto itemRefundDto = itemService.add(id, itemDto);

        assertEquals(itemRefundDto.getId(), 1L);
        assertEquals(itemRefundDto.getName(), "Ручной рубанок");
        assertEquals(itemRefundDto.getDescription(),
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм");
        assertEquals(itemRefundDto.getRequestId(), 1);

        verify(userDao).findById(anyLong());
        verify(requestDao).findById(anyLong());
        verify(itemDao).save(any());
    }

    @Test
    void add_whenUserNotData_thenReturnNotObjectException() {
        long id = 1L;
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируеться от 0мм до 3мм",
                true,
                id
        );

        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> itemService.add(id, itemDto), "нет пользователя");

        verify(userDao).findById(anyLong());
        verify(requestDao, never()).findById(anyLong());
        verify(itemDao, never()).save(any());
    }

    @Test
    void add_whenUserFound_AndRequestNotData_thenReturnNotObjectException() {
        long id = 1L;
        User user = new User(id, "23@mail.ru", "Jon");
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируеться от 0мм до 3мм",
                true,
                id
        );

        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> itemService.add(id, itemDto), "нет данного запроса");

        verify(userDao).findById(anyLong());
        verify(requestDao).findById(anyLong());
        verify(itemDao, never()).save(any());
    }

    // метод edit
    @Test
    void edit_whenOwnerFound_thenReturnItem() {
        long userId = 1L;
        long id = 1L;
        User user = new User(id, "23@mail.ru", "Jon");
        ItemRequest request = new ItemRequest(
                id,
                "нужен рубанок по дереву",
                user,
                LocalDateTime.now().minusDays(1)
        );
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок 'TurboM'",
                "Рубанок для работы по дереву",
                false,
                2L
        );
        Item item = new Item(
                1L,
                user,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );

        when(itemDao.findById(anyLong())).thenReturn(Optional.of(item));
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));

        ItemRefundDto itemRefundDto = itemService.edit(userId, id, itemDto);

        assertEquals(itemRefundDto.getId(), 1L);
        assertEquals(itemRefundDto.getName(), "Ручной рубанок 'TurboM'");
        assertEquals(itemRefundDto.getDescription(), "Рубанок для работы по дереву");
        assertEquals(itemRefundDto.getAvailable(), false);
        assertEquals(itemRefundDto.getRequestId(), 1);

        verify(userDao).findById(anyLong());
        verify(itemDao).findById(anyLong());
    }

    @Test
    void edit_whenNotOwnerFound_thenReturnNotOwnerException() {
        long userId = 2L;
        long id = 1L;
        User user = new User(id, "23@mail.ru", "Jon");
        ItemRequest request = new ItemRequest(
                id,
                "нужен рубанок по дереву",
                user,
                LocalDateTime.now().minusDays(1)
        );
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок 'TurboM'",
                "Рубанок для работы по дереву",
                true,
                id
        );
        Item item = new Item(
                1L,
                user,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );

        when(itemDao.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotOwnerException.class, () -> itemService.edit(userId, id, itemDto),
                "Вы не явдяетесь владельцем записи");

        verify(userDao, never()).findById(anyLong());
        verify(itemDao).findById(anyLong());

    }

    @Test
    void edit_whenNotUserData_thenNotObjectException() {
        long userId = 1L;
        long id = 1L;
        User user = new User(id, "23@mail.ru", "Jon");
        ItemRequest request = new ItemRequest(
                id,
                "нужен рубанок по дереву",
                user,
                LocalDateTime.now().minusDays(1)
        );
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок 'TurboM'",
                "Рубанок для работы по дереву",
                true,
                id
        );
        Item item = new Item(
                1L,
                user,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );

        when(itemDao.findById(anyLong())).thenReturn(Optional.of(item));
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> itemService.edit(userId, id, itemDto),
                "нет пользователя");

        verify(userDao).findById(anyLong());
        verify(itemDao).findById(anyLong());

    }

    @Test
    void edit_whenNotItemData_thenNotObjectException() {
        long userId = 1L;
        long id = 1L;
        ItemDto itemDto = new ItemDto(
                "Ручной рубанок 'TurboM'",
                "Рубанок для работы по дереву",
                true,
                id
        );

        when(itemDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> itemService.edit(userId, id, itemDto), "нет записи");

        verify(userDao, never()).findById(anyLong());
        verify(itemDao).findById(anyLong());

    }

    // метод findById
    @Test
    void findById_whenItemWithAllDataFound_thenReturnItem() {
        long userId = 1L;
        long itemId = 1L;
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 2L;
        User user1 = new User(id1, "23@mail.ru", "Jon");
        User user2 = new User(id2, "24@mail.ru", "Bob");
        User user3 = new User(id3, "25@mail.ru", "Sveta");
        ItemRequest request = new ItemRequest(
                id1,
                "нужен рубанок по дереву",
                user1,
                LocalDateTime.now().minusDays(1)
        );
        Item item = new Item(
                id1,
                user3,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                id1,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user1,
                Status.APPROVED
        );
        Booking booking2 = new Booking(
                id2,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                user2,
                Status.APPROVED
        );
        bookings.add(booking1);
        bookings.add(booking2);
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment(
                id1,
                "инструмент работает на 4, при долгой работе съезжает лезвие",
                item,
                user1,
                LocalDateTime.now().minusHours(4)
        );
        comments.add(comment1);
        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findItemByOwner(user1, itemId, Status.APPROVED)).thenReturn(bookings);
        when(commentDao.findByItem_Id(itemId)).thenReturn(comments);
        when(itemDao.findById(itemId)).thenReturn(Optional.of(item));

        ItemBookingDto itemBookingDto = itemService.findById(userId, itemId);

        assertEquals(itemBookingDto.getId(), 1L);
        assertEquals(itemBookingDto.getName(), "Ручной рубанок");
        assertEquals(itemBookingDto.getDescription(),
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм");
        assertTrue(itemBookingDto.getAvailable());
        assertEquals(itemBookingDto.getLastBooking().getId(), 1);
        assertEquals(itemBookingDto.getNextBooking().getId(), 2);
        assertEquals(itemBookingDto.getComments().size(), 1);

        verify(bookingDao).findItemByOwner(user1, itemId, Status.APPROVED);
        verify(commentDao).findByItem_Id(itemId);
        verify(itemDao).findById(itemId);
    }

    @Test
    void findById_whenItemWithoutDataFound_thenReturnItem() {
        long userId = 1L;
        long itemId = 1L;
        long id1 = 1L;
        long id3 = 2L;
        User user1 = new User(userId, "25@mail.ru", "Sveta");
        User user3 = new User(id3, "25@mail.ru", "Sveta");
        Item item = new Item(
                id1,
                user3,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                null
        );
        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findItemByOwner(user1, itemId, Status.APPROVED)).thenReturn(List.of());
        when(commentDao.findByItem_Id(itemId)).thenReturn(List.of());
        when(itemDao.findById(itemId)).thenReturn(Optional.of(item));

        ItemBookingDto itemBookingDto = itemService.findById(userId, itemId);

        assertEquals(itemBookingDto.getId(), 1L);
        assertEquals(itemBookingDto.getName(), "Ручной рубанок");
        assertEquals(itemBookingDto.getDescription(),
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм");
        assertTrue(itemBookingDto.getAvailable());
        assertNull(itemBookingDto.getLastBooking());
        assertNull(itemBookingDto.getNextBooking());
        assertTrue(itemBookingDto.getComments().isEmpty());

        verify(bookingDao).findItemByOwner(user1, itemId, Status.APPROVED);
        verify(commentDao).findByItem_Id(itemId);
        verify(itemDao).findById(itemId);
    }

    @Test
    void findById_whenNotItemData_thenReturnNotObjectException() {
        long userId = 1L;
        long itemId = 1L;
        User user1 = new User(userId, "25@mail.ru", "Sveta");
        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findItemByOwner(user1, itemId, Status.APPROVED)).thenReturn(List.of());
        when(commentDao.findByItem_Id(itemId)).thenReturn(List.of());
        when(itemDao.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> itemService.findById(userId, itemId), "нет записи");

        verify(bookingDao).findItemByOwner(user1, itemId, Status.APPROVED);
        verify(commentDao).findByItem_Id(itemId);
        verify(itemDao).findById(itemId);
    }

    // метод findAllForUser
    @Test
    void findAllForUser_whenItemWithAllDataFound_ReturnItems() {
        long userId = 3L;
        long id1 = 1L;
        long id2 = 2L;
        int page = 0;
        int size = 3;
        User user1 = new User(id1, "23@mail.ru", "Jon");
        User user2 = new User(id2, "24@mail.ru", "Bob");
        User user3 = new User(userId, "25@mail.ru", "Sveta");
        ItemRequest request = new ItemRequest(
                id1,
                "нужен рубанок по дереву",
                user1,
                LocalDateTime.now().minusDays(1)
        );
        List<Item> items = new ArrayList<>();
        Item item = new Item(
                id1,
                user3,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );
        items.add(item);
        PageImpl<Item> itemsPage = new PageImpl<>(items);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                id1,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user1,
                Status.APPROVED
        );
        Booking booking2 = new Booking(
                id2,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                user2,
                Status.APPROVED
        );
        bookings.add(booking1);
        bookings.add(booking2);
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment(
                id1,
                "инструмент работает на 4, при долгой работе съезжает лезвие",
                item,
                user1,
                LocalDateTime.now().minusHours(4)
        );
        comments.add(comment1);
        when(userDao.findById(userId)).thenReturn(Optional.of(user3));
        when(itemDao.findAllForUser(user3, PageRequest.of(page, size))).thenReturn(itemsPage);
        when(commentDao.findByItemInOrderByCreatedDesc(any())).thenReturn(comments);
        when(bookingDao.findAllItemsByOwner(user3, Status.APPROVED)).thenReturn(bookings);

        List<ItemBookingDto> itemsBookingDto = itemService.findAllForUser(page, size, userId);
        ItemBookingDto itemBookingDto = itemsBookingDto.get(0);

        assertEquals(itemBookingDto.getId(), 1L);
        assertEquals(itemBookingDto.getName(), "Ручной рубанок");
        assertEquals(itemBookingDto.getDescription(),
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм");
        assertTrue(itemBookingDto.getAvailable());
        assertEquals(itemBookingDto.getLastBooking().getId(), 1);
        assertEquals(itemBookingDto.getNextBooking().getId(), 2);
        assertEquals(itemBookingDto.getComments().size(), 1);

        verify(itemDao).findAllForUser(user3, PageRequest.of(page, size));
        verify(commentDao).findByItemInOrderByCreatedDesc(any());
        verify(bookingDao).findAllItemsByOwner(user3, Status.APPROVED);
    }

    @Test
    void findAllForUser_whenItemWithoutDataFound_ReturnItems() {
        long userId = 3L;
        long id1 = 1L;
        long id3 = 3L;
        int page = 0;
        int size = 3;
        User user3 = new User(id3, "25@mail.ru", "Sveta");
        List<Item> items = new ArrayList<>();
        Item item = new Item(
                id1,
                user3,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                null
        );
        items.add(item);
        PageImpl<Item> itemsPage = new PageImpl<>(items);
        when(userDao.findById(userId)).thenReturn(Optional.of(user3));
        when(itemDao.findAllForUser(user3, PageRequest.of(page, size))).thenReturn(itemsPage);
        when(commentDao.findByItemInOrderByCreatedDesc(any())).thenReturn(List.of());
        when(bookingDao.findAllItemsByOwner(user3, Status.APPROVED)).thenReturn(List.of());

        List<ItemBookingDto> itemsBookingDto = itemService.findAllForUser(page, size, userId);
        ItemBookingDto itemBookingDto = itemsBookingDto.get(0);

        assertEquals(itemBookingDto.getId(), 1L);
        assertEquals(itemBookingDto.getName(), "Ручной рубанок");
        assertEquals(itemBookingDto.getDescription(),
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм");
        assertTrue(itemBookingDto.getAvailable());
        assertNull(itemBookingDto.getLastBooking());
        assertNull(itemBookingDto.getNextBooking());
        assertTrue(itemBookingDto.getComments().isEmpty());

        verify(itemDao).findAllForUser(user3, PageRequest.of(page, size));
        verify(commentDao).findByItemInOrderByCreatedDesc(any());
        verify(bookingDao).findAllItemsByOwner(user3, Status.APPROVED);
    }

    @Test
    void findAllForUser_whenItemNotData_ReturnEmpty() {
        long userId = 3L;
        int page = 0;
        int size = 3;
        User user3 = new User(userId, "25@mail.ru", "Sveta");
        List<Item> items = new ArrayList<>();
        PageImpl<Item> itemsPage = new PageImpl<>(items);
        when(userDao.findById(userId)).thenReturn(Optional.of(user3));
        when(itemDao.findAllForUser(user3, PageRequest.of(page, size))).thenReturn(itemsPage);
        when(commentDao.findByItemInOrderByCreatedDesc(any())).thenReturn(List.of());
        when(bookingDao.findAllItemsByOwner(user3, Status.APPROVED)).thenReturn(List.of());

        List<ItemBookingDto> itemsBookingDto = itemService.findAllForUser(page, size, userId);

        assertTrue(itemsBookingDto.isEmpty());

        verify(itemDao).findAllForUser(user3, PageRequest.of(page, size));
        verify(commentDao).findByItemInOrderByCreatedDesc(any());
        verify(bookingDao).findAllItemsByOwner(user3, Status.APPROVED);
    }

    // метод search
    @Test
    void search_ReturnItems() {
        long id1 = 1L;
        long id3 = 3L;
        int page = 0;
        int size = 3;
        String text = "рубанок";
        User user3 = new User(id3, "25@mail.ru", "Sveta");
        List<Item> items = new ArrayList<>();
        Item item = new Item(
                id1,
                user3,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                null
        );
        items.add(item);
        PageImpl<Item> itemsPage = new PageImpl<>(items);
        when(itemDao.search(text, PageRequest.of(page, size))).thenReturn(itemsPage);

        List<ItemRefundDto> itemsRefundDto = itemService.search(text, page, size);
        ItemRefundDto itemRefundDto = itemsRefundDto.get(0);

        assertEquals(itemsRefundDto.size(), 1);
        assertEquals(itemRefundDto.getId(), 1);

        verify(itemDao).search(text, PageRequest.of(page, size));

    }

    // метод addComment
    @Test
    void addComment_thenReturnComment() {
        long userId = 1L;
        Long itemId = 1L;
        long id1 = 1L;
        long id3 = 3L;
        User user1 = new User(id1, "23@mail.ru", "Jon");
        User user3 = new User(id3, "25@mail.ru", "Sveta");
        ItemRequest request = new ItemRequest(
                id1,
                "нужен рубанок по дереву",
                user1,
                LocalDateTime.now().minusDays(1)
        );
        Item item = new Item(
                itemId,
                user3,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                id1,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user1,
                Status.APPROVED
        );
        bookings.add(booking1);
        CommentDto commentDto = new CommentDto(null,
                "инструмент работает на 4, при долгой работе съезжает лезвие");
        Comment comment = new Comment();
        comment.setId(id1);
        comment.setText("инструмент работает на 4, при долгой работе съезжает лезвие");
        comment.setItem(item);
        comment.setAuthor(user1);
        comment.setCreated(LocalDateTime.now());

        when(userDao.findById(any())).thenReturn(Optional.of(user1));
        when(itemDao.findById(any())).thenReturn(Optional.of(item));
        when(bookingDao.findByBooker_IdAndItem_Id(user1, itemId, Status.APPROVED)).thenReturn(bookings);
        when(commentDao.save(any())).thenReturn(comment);

        CommentRefundDto commentRefundDto = itemService.addComment(userId, itemId, commentDto);

        assertEquals(commentRefundDto.getId(), 1L);
        assertEquals(commentRefundDto.getText(), "инструмент работает на 4, при долгой работе съезжает лезвие");
        assertEquals(commentRefundDto.getAuthorName(), "Jon");

        verify(userDao).findById(any());
        verify(itemDao).findById(any());
        verify(bookingDao).findByBooker_IdAndItem_Id(user1, itemId, Status.APPROVED);
        verify(commentDao).save(any());
    }

    @Test
    void addComment_whenNotUserData_thenReturnNotObjectException() {
        long userId = 1L;
        long itemId = 1L;
        User user1 = new User(userId, "25@mail.ru", "Sveta");
        CommentDto commentDto = new CommentDto(null,
                "инструмент работает на 4, при долгой работе съезжает лезвие");

        when(userDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> itemService.addComment(userId, itemId, commentDto),
                "пользователь не найден");

        verify(userDao).findById(any());
        verify(itemDao, never()).findById(any());
        verify(bookingDao, never()).findByBooker_IdAndItem_Id(user1, itemId, Status.APPROVED);
        verify(commentDao, never()).save(any());
    }

    @Test
    void addComment_whenNotItemData_thenReturnNotObjectException() {
        long userId = 1L;
        long itemId = 1L;
        long id1 = 1L;
        User user1 = new User(id1, "23@mail.ru", "Jon");
        CommentDto commentDto = new CommentDto(null,
                "инструмент работает на 4, при долгой работе съезжает лезвие");

        when(userDao.findById(any())).thenReturn(Optional.of(user1));
        when(itemDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> itemService.addComment(userId, itemId, commentDto),
                "вещь не найдена");

        verify(userDao).findById(any());
        verify(itemDao).findById(any());
        verify(bookingDao, never()).findByBooker_IdAndItem_Id(user1, itemId, Status.APPROVED);
        verify(commentDao, never()).save(any());
    }

    @Test
    void addComment_NotBooking_thenReturnCommentException() {
        long userId = 1L;
        Long itemId = 1L;
        long id1 = 1L;
        long id3 = 3L;
        User user1 = new User(id1, "23@mail.ru", "Jon");
        User user3 = new User(id3, "25@mail.ru", "Sveta");
        ItemRequest request = new ItemRequest(
                id1,
                "нужен рубанок по дереву",
                user1,
                LocalDateTime.now().minusDays(1)
        );
        Item item = new Item(
                itemId,
                user3,
                "Ручной рубанок",
                "Рубанок для работы по дереву, лезвие регулируется от 0мм до 3мм",
                true,
                request
        );
        CommentDto commentDto = new CommentDto(null,
                "инструмент работает на 4, при долгой работе съезжает лезвие");

        when(userDao.findById(any())).thenReturn(Optional.of(user1));
        when(itemDao.findById(any())).thenReturn(Optional.of(item));
        when(bookingDao.findByBooker_IdAndItem_Id(user1, itemId, Status.APPROVED)).thenReturn(List.of());

        assertThrows(CommentException.class, () -> itemService.addComment(userId, itemId, commentDto),
                "нет бронирование на вещь у пользователя");

        verify(userDao).findById(any());
        verify(itemDao).findById(any());
        verify(bookingDao).findByBooker_IdAndItem_Id(user1, itemId, Status.APPROVED);
        verify(commentDao, never()).save(any());
    }
}