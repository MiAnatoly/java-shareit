package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.servece.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserRefundDto> findAll() {
        return service.findAll();
    }
    //показать всех пользователей

    @GetMapping("/{userId}")
    public UserRefundDto findById(
            @PathVariable long userId
    ) {
        log.info("Get users all");
        return service.findById(userId);
    }
    // показать пользователя по id

    @PatchMapping("/{userId}")
    public UserRefundDto edit(
            @PathVariable long userId,
            @RequestBody UserDto userDto
    ) {
        log.info("Get user with userId={}", userId);
        return service.edit(userId, userDto);
    }
    // внести изменению в данных пользователя

    @PostMapping
    public UserRefundDto add(
            @RequestBody UserDto userDto
    ) {
        log.info("Post user");
        return service.save(userDto);
    }
    // добавить пользователя

    @DeleteMapping("/{userId}")
    public void delete(
            @PathVariable long userId
    ) {
        log.info("Delete user with userId={}", userId);
        service.delete(userId);
    }
    // удалить пользователя
}
