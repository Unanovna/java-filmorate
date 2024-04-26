package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private HashMap<Long, User> users = new HashMap<Long, User>();

    @Getter
    private static Long idController = 1L;
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

    public Long generateId() {
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
    public User getById() throws ObjectNotFoundException {
        return getById(null);
    }

    public String deleteUserById(Long userId) {
        return "Пользователь user_id=" + userId + " успешно удален.";
    }

    @Override
    public boolean containsUser(Integer userId) {
        return false;
    }

    @Override
    public List<User> getCommonFriends(int friend1, int friend2) {
        return null;
    }

    @SneakyThrows
    @Override
    public User getById(Long userId) {
        isExist(userId);
        log.info("Пользователь {} возвращен", users.get(userId));
        return users.get(userId);
    }

    @SneakyThrows
    @Override
    public void deleteFriend(Long userId, Long friendId) {
        isExist(userId);
        isExist(friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", users.get(userId), users.get(friendId));
        users.get(userId).getFriendsList().remove(friendId);
        users.get(friendId).getFriendsList().remove(userId);
    }

    @Override
    public Collection<User> getFriends(Long userId) throws ObjectNotFoundException {
        isExist(userId);
        Collection<User> friendsList = new HashSet<>();
        if (users.get(userId).getFriendsList() != null && users.get(userId).getFriendsList().size() > 0) {
            for (Long id : users.get(userId).getFriendsList()) {
                friendsList.add(users.get(id));
            }
            log.info("Запрос получения списка друзей пользователя {} выполнен", userId);
            return friendsList;
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<User> getMutualFriends(Long userId, Long secondUserId) throws ObjectNotFoundException {
        log.info("Список общих друзей {} и {} отправлен", userId, secondUserId);
        isExist(userId);
        isExist(secondUserId);
        Collection<User> friendsList = new HashSet<>();
        for (Long id : users.get(userId).getFriendsList()) {
            if (users.get(secondUserId).getFriendsList().contains(id)) {
                friendsList.add(users.get(id));
            }
        }
        log.info("Список общих друзей {} и {} отправлен", userId, secondUserId);
        return friendsList;
    }

    public void isExist(Long userId) throws ObjectNotFoundException {
        if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException("Пользователя с таким " + userId + " не существует");
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = users.get(userId);
        user.addFriend(friendId);
    }
}
