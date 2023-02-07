package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemDao;
    private final UserRepository userDao;
    private final CommentRepository commentDao;
    private final BookingRepository bookingDao;

    @Transactional
    @Override
    public ItemDto add(long userId, ItemDto itemDto) {
        User user = userDao.findById(userId).orElseThrow(() -> new NotObjectException("нет пользователя"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemDao.save(item));
    }

    @Transactional
    @Override
    public ItemDto edit(long userId, long itemId, ItemDto itemDto) {
        Item item = itemDao.findById(itemId).orElseThrow(() -> new NotObjectException("нет записи"));
        if (item.getOwner().getId() != userId) {
            throw new NotOwnerException("Вы не явдяетесь владельцем записи");
        }
        User user = userDao.findById(userId).orElseThrow(() -> new NotObjectException("нет пользователя"));
        Item itemNew = ItemMapper.toItem(itemDto, user);
        if (itemNew.getName() == null || itemNew.getName().isBlank()) {
            itemNew.setName(item.getName());
        }
        if (itemNew.getDescription() == null || itemNew.getDescription().isBlank()) {
            itemNew.setDescription(item.getDescription());
        }
        if (itemNew.getIsAvailable() == null) {
            itemNew.setIsAvailable(item.getIsAvailable());
        }
        itemNew.setId(itemId);
        return ItemMapper.toItemDto(itemDao.save(itemNew));
    }

    @Override
    public ItemBookingDto findById(long userId, long itemId) {
        Booking nextBooking = bookingDao.nextBooking(userId, itemId);
        Booking lastBooking = bookingDao.lastBooking(userId, itemId);
        List<Comment> comments = commentDao.findByItem_Id(itemId);
        Item item = itemDao.findById(itemId).orElseThrow(() -> new NotObjectException("нет записи"));
        return ItemMapper.toItemBookingDto(nextBooking, lastBooking, comments, item);
    }

    @Override
    public List<ItemBookingDto> findAllForUser(long userId) {
        List<Item> items = itemDao.findAllForUser(userId);
        List<ItemBookingDto> itemsBooking = new ArrayList<>();
        for (Item item : items) {
            Booking nextBooking = bookingDao.nextBooking(userId, item.getId());
            Booking lastBooking = bookingDao.lastBooking(userId, item.getId());
            List<Comment> comments = commentDao.findByItem_Id(item.getId());
            itemsBooking.add(ItemMapper.toItemBookingDto(nextBooking, lastBooking, comments, item));
        }
        return itemsBooking;
    }

    @Override
    public List<ItemDto> search(String text) {
        return ItemMapper.toItemsDto(itemDao.search(text));
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userDao.findById(userId).orElseThrow(() -> new NotObjectException("пользователь не найден"));
        Item item = itemDao.findById(itemId).orElseThrow(() -> new NotObjectException("вещь не найдена"));
        List<Booking> bookings = bookingDao.findByBooker_IdAndItem_Id(userId, itemId,
                Status.APPROVED, LocalDateTime.now());
        if (!bookings.isEmpty()) {
            Comment comment = CommentMapper.toComment(user, item, commentDto);
            return CommentMapper.toCommentDto(commentDao.save(comment));
        } else {
            throw new CommentException("нет бронирование на вещь у пользователя");
        }
    }
}
