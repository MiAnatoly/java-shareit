package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    BookingRepository bookingDao;
    @Mock
    UserRepository userDao;
    @Mock
    ItemRepository itemDao;

    @InjectMocks
    BookingServiceImpl service;

    // метод add
    @Test
    void add_whenItemAndUserFound_thenReturnBooking() {
        Long userId = 1L;
        User user1 = new User(userId, "34@mail.ru", "Bob");
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user2,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user1,
                Status.WAITING
        );

        when(itemDao.findById(any())).thenReturn(Optional.of(item));
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingDao.save(any())).thenReturn(booking);

        BookingItemDto bookingItemDto = service.add(userId, bookingDto);

        assertEquals(bookingItemDto.getId(), 1L);
        assertEquals(bookingItemDto.getItem().getId(), 1L);
        assertEquals(bookingItemDto.getBooker().getId(), 1L);
        assertNotNull(bookingItemDto.getStart());
        assertNotNull(bookingItemDto.getEnd());
        assertEquals(bookingItemDto.getStatus(), Status.WAITING);
    }

    @Test
    void add_whenUserNotData_thenReturnNotObjectException() {
        Long userId = 1L;
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user2,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );

        when(itemDao.findById(any())).thenReturn(Optional.of(item));
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.add(userId, bookingDto),
                "отсутствует пользователь");

        verify(bookingDao, never()).save(any());
    }

    @Test
    void add_whenItemAvailableFalse_thenReturnNotAvailableException() {
        Long userId = 1L;
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user2,
                "ручка",
                "для рисования 3д моделей",
                false,
                null
        );

        when(itemDao.findById(any())).thenReturn(Optional.of(item));

        assertThrows(NotAvailableException.class, () -> service.add(userId, bookingDto),
                "нет доступа на вещь");

        verify(userDao, never()).findById(anyLong());
        verify(bookingDao, never()).save(any());
    }

    @Test
    void add_whenOwnerNotBooking_thenReturnNotBookingException() {
        Long userId = 1L;
        User user = new User(userId, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );

        when(itemDao.findById(any())).thenReturn(Optional.of(item));

        assertThrows(NotBookingException.class, () -> service.add(userId, bookingDto),
                "пользователь не может бронировать свою вещь");

        verify(userDao, never()).findById(anyLong());
        verify(bookingDao, never()).save(any());
    }

    @Test
    void add_whenItemNotData_thenReturnNotObjectException() {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );

        when(itemDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.add(userId, bookingDto),
                "отсутствует пользователь");

        verify(userDao, never()).findById(anyLong());
        verify(bookingDao, never()).save(any());
    }

    // метод approved
    @Test
    void approved_whenOwnerApproved_thenReturnBooking() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        User user1 = new User(userId, "34@mail.ru", "Bob");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user1,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user1,
                Status.WAITING
        );

        when(bookingDao.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingItemDto bookingItemDto = service.approved(userId, bookingId, approved);

        assertEquals(bookingItemDto.getId(), 1L);
        assertEquals(bookingItemDto.getItem().getId(), 1L);
        assertEquals(bookingItemDto.getBooker().getId(), 1L);
        assertNotNull(bookingItemDto.getStart());
        assertNotNull(bookingItemDto.getEnd());
        assertEquals(bookingItemDto.getStatus(), Status.APPROVED);
    }

    @Test
    void approved_whenOwnerRejected_thenReturnBooking() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = false;
        User user1 = new User(userId, "34@mail.ru", "Bob");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user1,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user1,
                Status.WAITING
        );

        when(bookingDao.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingItemDto bookingItemDto = service.approved(userId, bookingId, approved);

        assertEquals(bookingItemDto.getId(), 1L);
        assertEquals(bookingItemDto.getItem().getId(), 1L);
        assertEquals(bookingItemDto.getBooker().getId(), 1L);
        assertNotNull(bookingItemDto.getStart());
        assertNotNull(bookingItemDto.getEnd());
        assertEquals(bookingItemDto.getStatus(), Status.REJECTED);
    }

    @Test
    void approved_whenNotOwner_thenReturnNotOwnerException() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        User user1 = new User(userId, "34@mail.ru", "Bob");
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user2,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user1,
                Status.WAITING
        );

        when(bookingDao.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotOwnerException.class, () -> service.approved(userId, bookingId, approved),
                "не являетесь владельцем вещи");
    }

    @Test
    void approved_whenOwnerConfirmedEarlier_thenReturnStatusConfirmedException() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        User user1 = new User(userId, "34@mail.ru", "Bob");
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user1,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user2,
                Status.APPROVED
        );

        when(bookingDao.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(StatusConfirmedException.class, () -> service.approved(userId, bookingId, approved),
                "ответ по бранированию зафиксирован ранее");
    }

    @Test
    void approved_whenBookingNotData_thenReturnNotBookingException() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;

        when(bookingDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotBookingException.class, () -> service.approved(userId, bookingId, approved),
                "нет запроса");
    }

    // метод findById
    @Test
    void findById_whenOwnerItem_thenReturnBooking() {
        Long userId = 1L;
        Long bookingId = 1L;
        User user1 = new User(1L, "34@mail.ru", "Bob");
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user1,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user2,
                Status.WAITING
        );
        BookingItemDto bookingItemDto = new BookingItemDto();
        bookingItemDto.setId(1L);
        bookingItemDto.setItem(new BookingItemDto.Item(1L, "ручка"));
        bookingItemDto.setBooker(new BookingItemDto.Booker(2L, "BobRay"));
        bookingItemDto.setStart(LocalDateTime.now().plusHours(1));
        bookingItemDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingItemDto.setStatus(Status.WAITING);

        when(bookingDao.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingItemDto result = service.findById(userId, bookingId);

        assertEquals(result.getId(), bookingItemDto.getId());
        assertEquals(result.getItem().getId(), bookingItemDto.getItem().getId());
        assertEquals(result.getItem().getName(), bookingItemDto.getItem().getName());
        assertEquals(result.getBooker().getId(), bookingItemDto.getBooker().getId());
        assertEquals(result.getBooker().getName(), bookingItemDto.getBooker().getName());
        assertNotNull(result.getStart());
        assertNotNull(result.getEnd());
        assertEquals(result.getStatus(), bookingItemDto.getStatus());
    }

    @Test
    void findById_whenOwnerBooking_thenReturnBooking() {
        Long userId = 2L;
        Long bookingId = 1L;
        User user1 = new User(1L, "34@mail.ru", "Bob");
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user1,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user2,
                Status.WAITING
        );

        when(bookingDao.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingItemDto bookingItemDto = service.findById(userId, bookingId);

        assertEquals(bookingItemDto.getId(), 1L);
        assertEquals(bookingItemDto.getItem().getId(), 1L);
        assertEquals(bookingItemDto.getBooker().getId(), 2L);
        assertNotNull(bookingItemDto.getStart());
        assertNotNull(bookingItemDto.getEnd());
        assertEquals(bookingItemDto.getStatus(), Status.WAITING);
    }

    @Test
    void findById_whenNotOwner_thenReturnNotOwnerException() {
        Long userId = 3L;
        Long bookingId = 1L;
        User user1 = new User(1L, "34@mail.ru", "Bob");
        User user2 = new User(2L, "36@mail.ru", "BobRay");
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        Item item = new Item(
                1L,
                user1,
                "ручка",
                "для рисования 3д моделей",
                true,
                null
        );
        Booking booking = new Booking(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user2,
                Status.WAITING
        );

        when(bookingDao.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotOwnerException.class, () -> service.findById(userId, bookingId),
                "не являетесь автором брони или владельцем вещи");
    }

    // метод findAllForUser
    @Test
    void findAllForUser_whenStateAll_thenReturnBookings() {
        Long userId = 1L;
        State state = State.ALL;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(1L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingDao.findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForUser(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                userId, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size));
    }

    @Test
    void findAllForUser_whenStateCurrent_thenReturnBookings() {
        Long userId = 1L;
        State state = State.CURRENT;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(1L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingDao.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                anyLong(), any(), any(), any())).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForUser(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size));
        verify(bookingDao).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any());
        verify(bookingDao, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size));
    }

    @Test
    void findAllForUser_whenStatePast_thenReturnBookings() {
        Long userId = 1L;
        State state = State.PAST;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(1L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingDao.findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(),
                any())).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForUser(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                anyLong(), any(), any(), any());
        verify(bookingDao).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(),
                any());
        verify(bookingDao, never()).findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size));
    }

    @Test
    void findAllForUser_whenStateFuture_thenReturnBookings() {
        Long userId = 1L;
        State state = State.FUTURE;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(1L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingDao.findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(),
                any())).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForUser(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                userId, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(),
                any());
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size));
    }

    @Test
    void findAllForUser_whenStateWaiting_thenReturnBookings() {
        Long userId = 1L;
        State state = State.WAITING;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(1L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingDao.findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForUser(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                userId, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao).findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size));
    }

    @Test
    void findAllForUser_whenStateRejected_thenReturnBookings() {
        Long userId = 1L;
        State state = State.REJECTED;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(1L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingDao.findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForUser(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                userId, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size));
        verify(bookingDao).findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size));
    }

    @Test
    void findAllForUser_whenUserNotData_thenReturnNotObjectException() {
        Long userId = 1L;
        State state = State.REJECTED;
        int page = 0;
        int size = 5;

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.findAllForUser(userId, state, page, size),
                "нет пользователя");

        verify(bookingDao, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                userId, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.WAITING,
                PageRequest.of(page, size));
        verify(bookingDao, never()).findByBooker_IdAndStatus(userId, Status.REJECTED,
                PageRequest.of(page, size));
    }

    // метод findAllForOwner
    @Test
    void findAllForOwner_whenStateAll_thenReturnBookings() {
        Long userId = 1L;
        State state = State.ALL;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(2L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findAllOwner(user1, PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForOwner(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao).findAllOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findCurrentOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findPastOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findFutureOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size));
    }

    @Test
    void findAllForOwner_whenStateCurrent_thenReturnBookings() {
        Long userId = 1L;
        State state = State.CURRENT;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(2L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findCurrentOwner(user1, PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForOwner(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findAllOwner(user1, PageRequest.of(page, size));
        verify(bookingDao).findCurrentOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findPastOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findFutureOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size));
    }

    @Test
    void findAllForOwner_whenStatePast_thenReturnBookings() {
        Long userId = 1L;
        State state = State.PAST;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(2L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findPastOwner(user1, PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForOwner(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findAllOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findCurrentOwner(user1, PageRequest.of(page, size));
        verify(bookingDao).findPastOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findFutureOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size));
    }

    @Test
    void findAllForOwner_whenStateFuture_thenReturnBookings() {
        Long userId = 1L;
        State state = State.FUTURE;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(1L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findFutureOwner(user1, PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForOwner(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findAllOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findCurrentOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findPastOwner(user1, PageRequest.of(page, size));
        verify(bookingDao).findFutureOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size));
    }

    @Test
    void findAllForOwner_whenStateWaiting_thenReturnBookings() {
        Long userId = 1L;
        State state = State.WAITING;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(2L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForOwner(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findAllOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findCurrentOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findPastOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findFutureOwner(user1, PageRequest.of(page, size));
        verify(bookingDao).findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size));
    }

    @Test
    void findAllForOwner_whenStateRejected_thenReturnBookings() {
        Long userId = 1L;
        State state = State.REJECTED;
        int page = 0;
        int size = 5;
        User user1 = new User(1L, "245@mail", "Liza");
        User user2 = new User(2L, "2454@mail", "Liza2");
        Item item1 = new Item(1L, user2, "телефон", "телефон спутниковый",
                true, null);
        Item item2 = new Item(2L, user2, "телефон2", "телефон спутниковый2",
                true, null);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item1,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item2,
                user1,
                Status.WAITING
        );
        bookings.add(booking1);
        bookings.add(booking2);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);

        when(userDao.findById(userId)).thenReturn(Optional.of(user1));
        when(bookingDao.findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size))).thenReturn(bookingsPage);

        List<BookingItemDto> bookingsDto = service.findAllForOwner(userId, state, page, size);

        assertEquals(bookingsDto.size(), 2);

        verify(bookingDao, never()).findAllOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findCurrentOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findPastOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findFutureOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size));
        verify(bookingDao).findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size));
    }

    @Test
    void findAllForOwner_whenUserNotData_thenReturnNotObjectException() {
        Long userId = 1L;
        State state = State.REJECTED;
        User user1 = new User(1L, "245@mail", "Liza");
        int page = 0;
        int size = 5;

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> service.findAllForOwner(userId, state, page, size));

        verify(bookingDao, never()).findAllOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findCurrentOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findPastOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findFutureOwner(user1, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.WAITING, PageRequest.of(page, size));
        verify(bookingDao, never()).findStatusOwner(user1, Status.REJECTED, PageRequest.of(page, size));
    }
}