package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private HashMap<Integer, User> users = new HashMap<>();

    @Getter
    private static Integer idController = 1;
    private Logger log;

    @SneakyThrows
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    public User addUser(User user) {
        user.setId(idController);
        if (users.containsValue(user)) {
            log.trace("Данный пользователь уже добавлен в систему");
            throw new ValidateException("Данный пользователь уже добавлен в систему");
        }
        users.put(user.getId(), user);
        generateId();
        return user;
    }

    public Integer generateId() {
        return ++idController;
    }


    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    @SneakyThrows
    @Override
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.trace("Обновление невозможно - пользователь с указанным id " + user.getId() + " отсутствует в системе");
            throw new ValidateException("Обновление невозможно - пользователь с указанным id " + user.getId() + " отсутствует в системе");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public String delete(int id) {
        return null;
    }

    @Override
    public User getById(Integer id) {
        return null;
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {

    }

    @Override
    public boolean containsUser(Integer userId) {
        return false;
    }

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
