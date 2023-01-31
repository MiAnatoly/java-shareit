package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();

    User findById(long id);

    User save(User user);

    User edit(long userId, User user);

    void deleteUser(Long id);
}
