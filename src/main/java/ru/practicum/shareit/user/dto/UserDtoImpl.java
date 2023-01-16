package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDtoImpl implements UserDto {
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
        List<User> userList = new ArrayList<>(users.values());
        if (userList.stream().noneMatch(x -> x.getEmail().equals(user.getEmail()))) {
            ++id;
            user.setId(id);
            users.put(id, user);
            return user;
        } else {
            throw new DuplicateException("позьзователь с данной почтой уже зарегистрирован");
        }
    }

    @Override
    public User edit(long userId, User user) {
        User user1 = users.get(userId);
        if (user.getName() != null) {
            user1.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (users.values().stream().noneMatch(x -> x.getEmail().equals(user.getEmail()))) {
                user1.setEmail(user.getEmail());
            } else {
                throw new DuplicateException("позьзователь с данной почтой уже зарегистрирован");
            }
        }
        return user1;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }
}
