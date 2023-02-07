package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.servece.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> findAll() {
        return service.findAll();
    }
    //показать всех пользователей

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable long userId) {
        return service.findById(userId);
    }
    // показать пользователя по id

    @PatchMapping("/{userId}")
    public UserDto edit(@PathVariable long userId, @Validated({Update.class}) @RequestBody UserDto userDto) {
        return service.edit(userId, userDto);
    }
    // внести изменению в данных пользователя

    @PostMapping
    public UserDto add(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return service.save(userDto);
    }
    // добавить пользователя

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        service.delete(userId);
    }
    // удалить пользователя
}
