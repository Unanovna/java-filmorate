package ru.yandex.practicum.filmorate.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RequestMapping("/users")
@AllArgsConstructor
@RestController
public class UserController {
    private UserService userService;


    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public Collection<User> getMutualFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.getMutualFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }


    @DeleteMapping("/{userId}")
    public String deleteUserById(@PathVariable("userId") Long userId) {
        return userService.deleteUserById(userId);
    }

    public void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.trace("email не может быть пустым");
            throw new ValidateException("email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.trace("email должен содержать символ @");
            throw new ValidateException("email должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.trace("логин не может быть пустым и содержать пробелы");
            throw new ValidateException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getLogin().contains(" ")) {
            log.trace("логин не может быть пустым и содержать пробелы");
            throw new ValidateException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("дата рождения не может быть в будущем");
            throw new ValidateException("дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("вместо имени пользователя будет использоваться логин");
            user.setName(user.getLogin());
        }
    }
}
