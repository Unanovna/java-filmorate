package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    @Test
    public void shouldThrowException() {
        UserController controller = new UserController();
        User user1 = new User("sobaka1@mail.com", "log in", "2000-12-25");
        final ValidateException exp1 = assertThrows(
                ValidateException.class,
                () -> controller.validate(user1)
        );
        Assertions.assertEquals("логин не может быть пустым и содержать пробелы", exp1.getMessage());

        User user2 = new User("sobaka2@mail.com", " ", "2000-11-25");
        final ValidateException exp2 = assertThrows(
                ValidateException.class,
                () -> controller.validate(user2)
        );
        Assertions.assertEquals("логин не может быть пустым и содержать пробелы", exp2.getMessage());

        User user3 = new User("sobaka3@mail.com", "login3", "3000-12-25");
        final ValidateException exp3 = assertThrows(
                ValidateException.class,
                () -> controller.validate(user3)
        );
        Assertions.assertEquals("дата рождения не может быть в будущем", exp3.getMessage());
    }
}