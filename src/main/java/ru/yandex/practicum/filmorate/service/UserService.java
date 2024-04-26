package ru.yandex.practicum.filmorate.service;

import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;
    private final FilmStorage filmStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public Collection<User> getAllUsers() {
        log.info("Список всех пользователей: " + userStorage.getAllUsers().size());
        return userStorage.getAllUsers();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public String deleteUserById(Long userId) {
        return userStorage.deleteUserById(userId);
    }


    public User getById(Long userId) {
        return userStorage.getById(userId);
    }

    @SneakyThrows
    public User addFriend(Long userId, Long friendId) {
        userStorage.isExist(userId);
        userStorage.isExist(friendId);
        feedStorage.addEvent(userId, EventType.FRIEND, OperationType.ADD, friendId);
        return userStorage.addFriend(userId, friendId);
    }

    @SneakyThrows
    public Collection<User> getMutualFriends(Long userId, Long secondUserId) {
        return userStorage.getMutualFriends(userId, secondUserId);
    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    @SneakyThrows
    public void deleteFriend(Long userId, Long friendId) {
        userStorage.isExist(userId);
        userStorage.isExist(friendId);
        feedStorage.addEvent(userId, EventType.FRIEND, OperationType.REMOVE, friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    @SneakyThrows
    public Collection<Event> getFeedById(Long userId) {
        userStorage.isExist(userId);
        return feedStorage.getFeedById(userId);
    }
}