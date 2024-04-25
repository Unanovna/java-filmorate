package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

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
    private final FriendStorage friendStorage;
    private final FeedStorage feedStorage;
    private Integer friendId;
    private Integer userId;
    private String message;
    private User user;


    public User addUser(User user) {
        validate(user, "Форма пользователя заполнена неверно");
        preSave(user);
        User result = userStorage.addUser(user);
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


    public String deleteUserById(Long userId) {
        return userStorage.deleteUserById(userId);
    }

    public User getById(Long userId) throws ObjectNotFoundException {
        return userStorage.getById(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        checkUser(userId, friendId);
        userStorage.addFriend(userId, friendId);

        log.info("Друг успешно добавлен");
    }

    public Collection<User> getMutualFriends(Long userId, Long secondUserId) throws ObjectNotFoundException {
        return userStorage.getMutualFriends(userId, secondUserId);
    }

    public Collection<User> getFriends(Long userId) throws ObjectNotFoundException {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        containsUser(userId);
        containsUser(otherUserId);
        List<User> result = friendStorage.getCommonFriends(userId, otherUserId);
        log.info("Common friends of users with ID " + " {} and {} {} ", userId, otherUserId, result);
        return result;
    }

    private void containsUser(Integer userId) {
    }

    private void checkUser(Long userId, Long friendId) {
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

    public void deleteFriend(Integer id, Integer friendId) {
    }
}