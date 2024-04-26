package ru.yandex.practicum.filmorate.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static Long userId = 1L;

    @SneakyThrows
    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с такой почтой уже существует");
        } else {
            validate(user);
            user.setId(userId++);
            user.setFriendIds(new HashSet<>());
            log.info("Добавлен пользователь:{}", user);
            users.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @SneakyThrows
    @Override
    public User updateUser(User user) {
        isExist(user.getId());
        if (users.get(user.getId()).equals(user)) {
            throw new NotFoundException(String
                    .format("Такой пользователь уже существует c ID = %s", user.getId()));
        }
        log.info("Обновлен пользователь:{}", user);
        users.replace(user.getId(), user);
        return user;
    }

    @SneakyThrows
    @Override
    public User getById(Long userId) {
        isExist(userId);
        log.info("Пользователь {} возвращен", users.get(userId));
        return users.get(userId);
    }

    public String deleteUserById(Long userId) {
        users.remove(userId);
        return "Пользователь user_id=" + userId + " успешно удален.";
    }

    @SneakyThrows
    @Override
    public void deleteFriend(Long userId, Long friendId) {
        isExist(userId);
        isExist(friendId);
        User currentUser = users.get(userId);
        User friendUser = users.get(friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", currentUser, friendUser);
        currentUser.removeFriend(friendId);
    }

    @SneakyThrows
    @Override
    public Collection<User> getFriends(Long userId) {
        isExist(userId);
        User currentUser = users.get(userId);

        if (CollectionUtils.isEmpty(currentUser.getFriendIds())) {
            return Collections.emptyList();
        }

        return users.values().stream()
                .filter(user -> currentUser.getFriendIds().contains(user.getId()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public Collection<User> getCommonFriends(Long userId, Long secondUserId) {
        isExist(userId);
        isExist(secondUserId);

        Set<Long> firstUserFriends = users.get(userId).getFriendIds();
        Set<Long> secondUserFriends = users.get(secondUserId).getFriendIds();

        if (CollectionUtils.isEmpty(firstUserFriends) || CollectionUtils.isEmpty(secondUserFriends)) {
            return Collections.emptyList();
        }

        Set<Long> commonFriendIds = firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .collect(Collectors.toSet());

        return users.values().stream()
                .filter(user -> commonFriendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public void isExist(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователя с таким " + userId + " не существует");
        }
    }

    @SneakyThrows
    @Override
    public User addFriend(Long userId, Long friendId) {
        isExist(userId);
        isExist(friendId);
        User currentUser = users.get(userId);
        if (currentUser.getFriendIds().contains(friendId)) {
            throw new NotFoundException("Пользователь уже добавлен в друзья");
        }
        currentUser.addFriend(friendId);
        User friendUser = users.get(friendId);
        log.info("Пользователь {} добавлен в друзья пользователю {}", friendUser, currentUser);
        return friendUser;
    }


    //@Override
    //public void addFriend(Long userId, Long friendId) {
    //User user = users.get(userId);
    //user.addFriend(friendId);}

    private void validate(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
    }
}
