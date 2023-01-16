package ru.practicum.shareit.user.servece;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDto repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User findById(long id) {
        return repository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User edit(long userId, User user){
      return repository.edit(userId, user);
    }

    @Override
    public void deleteUser(Long userId){
        repository.deleteUser(userId);
    }
}
