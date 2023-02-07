package ru.practicum.shareit.user.servece;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(long id);

    UserDto save(UserDto userDto);

    UserDto edit(long userId, UserDto userDto);

    void delete(Long userId);
}
