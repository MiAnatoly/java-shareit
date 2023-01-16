package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.servece.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.getAllUsers();
    }
    //показать всех пользователей

    @GetMapping("/{userId}")
    public User findById(@PathVariable long userId) {
        return userService.findById(userId);
    }
    // показать пользователя по id

    @PatchMapping("/{userId}")
    public User edit(@PathVariable long userId, @RequestBody User user) {
        return userService.edit(userId, user);
    }

    // внести изменению в данных пользователя
    @PostMapping
    public User add(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    // добавить пользователя
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
    // удалить пользователя
}
