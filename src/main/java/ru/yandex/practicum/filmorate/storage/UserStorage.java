package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@SuppressWarnings("checkstyle:EmptyLineSeparator")
public interface UserStorage {
    User createUser(User user);

    Collection<User> getAllUsers();

    User updateUser(User user);

    User getById(Long userId);;

    void deleteFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    Collection<User> getMutualFriends(Long userId, Long secondUserId);

    User addFriend(Long userId, Long friendId);

    void isExist(Long userId);

    String deleteUserById(Long userId);
}
