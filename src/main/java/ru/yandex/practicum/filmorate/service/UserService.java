package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private Integer friendId;
    private Integer userId;
    private String message;
    private User user;


    public User addUser(User user) {
        validate(user, "Форма пользователя заполнена неверно");
        preSave(user);
        User result = UserStorage.addUser(user);
        log.info("Пользователь успешно добавлен: " + user);
        return result;
    }

    public Collection<User> getAllUsers() {
        log.info("Список всех пользователей: " + userStorage.getAllUsers().size());
        return userStorage.getAllUsers();
    }

    public User update(User user) {
        this.user = user;
        validate(user, "Форма обновления пользователя заполнена неверно");
        preSave(user);
        User result = userStorage.update(user);
        log.info("Пользователь успешно обновлен: " + user);
        return result;
    }

    public void delete(int userId) {
        if (getById(userId) == null) {
            throw new NotFoundException("Пользователь ID = " + userId + " не найден");
        }
        log.info("Удаление фильма с id: {}", userId);
        userStorage.delete(userId);
    }

    public User getById(Integer id) {
        log.info("Запрошен пользователь с ID = " + id);
        return userStorage.getById(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        userStorage.addFriend(userId, friendId);

        log.info("Друг успешно добавлен");
    }

    public void removeFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        userStorage.removeFriend(userId, friendId);
        log.info("Друг успешно удален");
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        containsUser(userId);
        containsUser(otherUserId);
        List<User> result = userStorage.getCommonFriends(userId, otherUserId);
        log.info("Common friends of users with ID " + " {} and {} {} ", userId, otherUserId, result);
        return result;
    }

    public List<User> getAllFriends(Integer userId) {
        containsUser(userId);
        List<User> result = userStorage.getFriends(userId);
        log.info("Friends of user with ID = " + userId + result);
        return result;
    }

    private void checkUser(Integer userId, Integer friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);
    }

    private static void validate(User user, String message) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }

    private static void preSave(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void containsUser(int id) {
        if (!userStorage.containsUser(id)) {
            throw new NotFoundException("User with id=" + id + " not exist. ");
        }
    }
}