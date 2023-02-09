package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User toUser(Long userId, UserDto userDto) {
        return new User(
                userId,
                userDto.getEmail(),
                userDto.getName()
        );
    }

    public static UserRefundDto toUserDto(User user) {
        return new UserRefundDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static List<UserRefundDto> toUsersDto(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}

