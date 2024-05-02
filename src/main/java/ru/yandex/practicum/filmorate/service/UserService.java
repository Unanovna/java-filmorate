package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userDbStorage;

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }

    public Collection<User> getAllUsers() {
        Collection<User> allUsers = userDbStorage.getAllUsers();
        log.info("Список всех пользователей: {}", allUsers.size());
        return allUsers;
    }

    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }

    public String deleteUserById(Long userId) {
        return userDbStorage.deleteUserById(userId);
    }

    public User getById(Long userId) {
        return userDbStorage.getById(userId);
    }

    @SneakyThrows
    public User addFriend(Long userId, Long friendId) {
        return userDbStorage.addFriend(userId, friendId);
    }

    @SneakyThrows
    public Collection<User> getCommonFriends(Long userId, Long secondUserId) {
        return userDbStorage.getCommonFriends(userId, secondUserId);
    }

    public Collection<User> getFriends(Long userId) {
        return userDbStorage.getFriends(userId);
    }

    @SneakyThrows
    public void deleteFriend(Long userId, Long friendId) {
        userDbStorage.deleteFriend(userId, friendId);
    }
}