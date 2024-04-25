package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user);

    Collection<User> getAllUsers();

    User update(User user);

    String delete(int id);

    User getById(Integer id);

    void removeFriend(Integer userId, Integer friendId);

    boolean containsUser(Integer userId);

    void addFriend(Integer userId, Integer friendId);
}
