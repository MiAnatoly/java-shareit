package ru.practicum.shareit.user.servece;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User findById(long id);
    User saveUser(User user);
    User edit(long userId, User user);
    void deleteUser(Long userId);
}
