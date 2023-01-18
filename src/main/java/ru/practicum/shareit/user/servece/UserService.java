package ru.practicum.shareit.user.servece;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto findById(long id);

    UserDto saveUser(UserDto userDto);

    UserDto edit(long userId, UserDto userDto);

    void deleteUser(Long userId);
}
