package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User addUser(User user);

    Collection<User> getAllUsers();

    User update(User user);

    User getById() throws ObjectNotFoundException;

    User getById(Long userId);

    void deleteFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long userId) throws ObjectNotFoundException;

    Collection<User> getMutualFriends(Long userId, Long secondUserId) throws ObjectNotFoundException;

    void addFriend(Long userId, Long friendId);

    void isExist(Long userId) throws ObjectNotFoundException;

    String deleteUserById(Long userId);

    default boolean containsUser(Integer userId) {
        return false;
    }

    List<User> getCommonFriends(int friend1, int friend2);
}
