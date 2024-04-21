package ru.yandex.practicum.filmorate.controller;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class UserController {

    @Getter
    private HashMap<Integer, User> users = new HashMap<>();
    @Getter
    private static Integer idController = 1;

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        validate(user);
        user.setId(idController);
        if (users.containsValue(user)) {
            log.trace("Данный пользователь уже добавлен в систему");
            throw new ValidateException("Данный пользователь уже добавлен в систему");
        }
        users.put(user.getId(), user);
        generateId();
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            log.trace("Обновление невозможно - пользователь с указанным id " + user.getId() + " отсутствует в системе");
            throw new ValidateException("Обновление невозможно - пользователь с указанным id " + user.getId() + " отсутствует в системе");
        }
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        return users.values();
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
        if (user.getName().isBlank()) {
            log.trace("вместо имени пользователя будет использоваться логин");
            user.setName(user.getLogin());
        }
        if (user.getName() == "null") {
            log.trace("вместо имени пользователя будет использоваться логин");
            user.setName(user.getLogin());
        }

    }

    public Integer generateId() {
        return ++idController;
    }
}
