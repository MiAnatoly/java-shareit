package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDto {
    List<User> findAll();
    User findById(long id);
    User save(User user);
    User edit(long userId, User user);
    void deleteUser(Long id);
}
