package ru.yandex.practicum.filmorate.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.ObjectNotFoundException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private final FeedService feedService;


    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);

    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        userService.update(user);
        return user;
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        feedService.addEvent(Long.valueOf(id), EventType.FRIEND,
                OperationType.ADD, Long.valueOf(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
        feedService.addEvent(Long.valueOf(id), EventType.FRIEND,
                OperationType.REMOVE, Long.valueOf(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) throws ObjectNotFoundException {
        return (List<User>) userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriends(id, otherId);
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
