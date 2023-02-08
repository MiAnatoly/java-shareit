package ru.practicum.shareit.user.servece;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;

import java.util.List;

public interface UserService {
    List<UserRefundDto> findAll();

    UserRefundDto findById(long id);

    UserRefundDto save(UserDto userDto);

    UserRefundDto edit(long userId, UserDto userDto);

    void delete(Long userId);
}
