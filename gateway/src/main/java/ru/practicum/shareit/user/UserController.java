package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Get users all");
        return userClient.findAll();
    }
    //показать всех пользователей

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(
            @PathVariable long userId
    ) {
        log.info("Get user with userId={}", userId);
        return userClient.findById(userId);
    }
    // показать пользователя по id

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> edit(
            @PathVariable long userId,
            @Validated({Update.class}) @RequestBody UserDto userDto
    ) {
        log.info("Patch user with userId={}", userId);
        return userClient.edit(userId, userDto);
    }
    // внести изменению в данных пользователя

    @PostMapping
    public ResponseEntity<Object> add(
            @Validated({Create.class}) @RequestBody UserDto userDto
    ) {
        log.info("Post user");
        return userClient.save(userDto);
    }
    // добавить пользователя

    @DeleteMapping("/{userId}")
    public void delete(
            @PathVariable long userId
    ) {
        log.info("Delete user with userId={}", userId);
        userClient.delete(userId);
    }
    // удалить пользователя
}
