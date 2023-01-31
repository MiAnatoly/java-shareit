package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.servece.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserService userService;

    @Override
    public ItemDto add(long userId, ItemDto itemDto) {
        User owner = UserMapper.toUser(userService.findById(userId));
        return ItemMapper.toItemDto(itemDao.add(userId, ItemMapper.toItem(itemDto, owner)));
    }

    @Override
    public ItemDto edit(long userId, long itemId, ItemDto itemDto) {
        if (itemDao.findById(itemId).getOwner().getId() != userId) {
            throw new NotOwnerException("Вы не явдяетесь владельцем записи");
        }
        User owner = UserMapper.toUser(userService.findById(userId));
        return ItemMapper.toItemDto(itemDao.edit(userId, itemId, ItemMapper.toItem(itemDto, owner)));
    }

    @Override
    public ItemDto findById(long itemId) {
        return ItemMapper.toItemDto(itemDao.findById(itemId));
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        return ItemMapper.toItemsDto(itemDao.findAll(userId));
    }

    @Override
    public List<ItemDto> search(String text) {
        return ItemMapper.toItemsDto(itemDao.search(text));
    }
}
