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
        idController++;
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
        if (user.getLogin().isBlank()) {
            log.trace("логин не может быть пустым и содержать пробелы");
            throw new ValidateException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getLogin().contains(" ")) {
            log.trace("логин не может быть пустым и содержать пробелы");
            throw new ValidateException("логин не может быть пустым и содержать пробелы");
        }
        String dateToString = String.valueOf(user.getBirthday());
        String [] split = dateToString.split("-");
        LocalDate date = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        if (date.isAfter(LocalDate.now())) {
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
