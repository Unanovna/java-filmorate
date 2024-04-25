package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class InMemoryFriendStoragee implements FriendStorage {
    @Override
    public void addFriend(int userID, int friendId) {

    }

    @Override
    public void removeFriend(int userID, int friendId) {

    }

    @Override
    public List<User> getFriends(int userId) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(int friend1, int friend2) {
        return null;
    }

    @Override
    public boolean isFriend(int userId, int friendId) {
        return false;
    }
}
