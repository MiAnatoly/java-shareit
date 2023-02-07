package ru.practicum.shareit.user.servece;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> findAll() {
        return UserMapper.toUsersDto(repository.findAll(Pageable.ofSize(3)).toList());
    }

    @Override
    public UserDto findById(long id) {
        return UserMapper.toUserDto(repository.findById(id)
                .orElseThrow(() -> new NotObjectException("нет пользователя")));
    }

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(null, userDto)));
    }

    @Transactional
    @Override
    public UserDto edit(long userId, UserDto userDto) {
        User userNew = UserMapper.toUser(userId, userDto);
        User userOld = repository.getOne(userId);
        if (userNew.getName() == null || userNew.getName().isBlank()) {
            userNew.setName(userOld.getName());
        }
        if (userNew.getEmail() == null || userNew.getEmail().isBlank()) {
            userNew.setEmail(userOld.getEmail());
        }
        userNew.setId(userId);
        return UserMapper.toUserDto(repository.save(userNew));
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }
}
