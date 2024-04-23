package ru.yandex.practicum.filmorate.storage;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
@SuppressWarnings("checkstyle:EmptyLineSeparator")
@Component
public interface UserStorage extends FriendStorage {


    @SuppressWarnings(value = "checkstyle:MethodParamPad")
    static User addUser(User user) {
        return null;
    }

    @SneakyThrows
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    default User addUser() {
        return addUser(null);
    }

    Collection<User> getAllUsers();

    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    static User update() {
        return null;
    }

    @SneakyThrows
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    User update(@Valid @RequestBody User user);
    String delete(int id);

    User getById(Integer id);

    void removeFriend(Integer userId, Integer friendId);

    boolean containsUser(Integer userId);
}
