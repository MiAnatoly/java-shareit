package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    private long id;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotObjectException("нет пользователя");
        }
    }

    @Override
    public User save(User user) {
        validate(user);
        ++id;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User edit(long userId, User user) {
        User userOld = users.get(userId);
        if (user.getName() != null && !user.getName().isBlank()) {
            userOld.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            if (!user.getEmail().equals(userOld.getEmail())) {
                validate(user);
                userOld.setEmail(user.getEmail());
            }
        }
        return userOld;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private void validate(User user) {
        if (users.values().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new DuplicateException("позьзователь с данной почтой уже зарегистрирован");
        }
    }
}
