package storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.db.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@ContextConfiguration(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private User user;
    private UserDbStorage userStorage;

    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail("kkkker@eew.ru");
        user.setLogin("va3fefef3");
        user.setName("Ken Ben");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    void getAllUsers_success() throws EntityAlreadyExistException {
        //given
        userStorage.createUser(user);
        //when
        Collection<User> allUsers = userStorage.getAllUsers();
        //then
        assertThat(allUsers)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getAllUsers_success_noUsers() throws EntityAlreadyExistException {
        //when
        Collection<User> allUsers = userStorage.getAllUsers();
        //then
        assertThat(allUsers)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void getUser_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        //when
        User storedUser = userStorage.getById(id);
        //then
        assertThat(storedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    void getUser_failure_wrongId() throws EntityAlreadyExistException {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.getById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void addUser_success() throws EntityAlreadyExistException {
        //when
        Long id = userStorage.createUser(user).getId();
        //then
        assertThat(id)
                .isNotNull()
                .isInstanceOf(Long.class)
                .isNotNegative();
    }

    @Test
    void addUser_failure() throws EntityAlreadyExistException {
        //given
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setLogin(user.getLogin());
        newUser.setName(user.getName());
        newUser.setBirthday(user.getBirthday());
        //when
        userStorage.createUser(user);
        //then
        assertThatThrownBy(() ->
                userStorage.createUser(newUser))
                .isInstanceOf(EntityAlreadyExistException.class)
                .hasMessageContaining("user already exists");
    }

    @Test
    void updateUser_success() throws EntityNotFoundException, EntityAlreadyExistException {
        //given
        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setName(user.getName());
        newUser.setBirthday(user.getBirthday());
        //when
        String newEmail = "newemail@new.com";
        newUser.setEmail(newEmail);
        Long id = userStorage.createUser(user).getId();
        User updatedUser = userStorage.updateUser(newUser);
        //then
        assertThat(updatedUser)
                .isNotNull()
                .isNotEqualTo(user);
        assertThat(updatedUser.getId())
                .isNotNull()
                .isEqualTo(user.getId());
    }

    @Test
    void updateUser_failure_wrongId() throws EntityNotFoundException {
        //given
        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setName(user.getName());
        newUser.setBirthday(user.getBirthday());
        //when
        String newEmail = "newemail@new.com";
        newUser.setEmail(newEmail);
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.updateUser(newUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void addFriend_success() throws EntityAlreadyExistException {
        //when
        Long id = userStorage.createUser(user).getId();
        //then
        assertThat(id)
                .isNotNull()
                .isInstanceOf(Long.class)
                .isNotNegative();
    }

    @Test
    void addFriend_failure_withWrongId() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        //when
        Long newId = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.addFriend(id, newId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void removeFriend_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        String newLogin = "newlogin";
        user.setLogin(newLogin);
        Long newId = userStorage.createUser(user).getId();
        //when
        userStorage.addFriend(id, newId);
        Set<User> friends = (Set<User>) userStorage.getFriends(id);
        userStorage.deleteFriend(id, newId);
        Set<User> friendsRemoved = (Set<User>) userStorage.getFriends(id);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(1);
        assertThat(friendsRemoved)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void removeFriend_failure_withWrongId() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        //when
        Long wrongId = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.deleteFriend(id, wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void removeFriend_failure_notFriends() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        //when
        String newLogin = "newlogin";
        user.setLogin(newLogin);
        Long newId = userStorage.createUser(user).getId();
        //then
        assertThatThrownBy(() ->
                userStorage.deleteFriend(id, newId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("friends are not found");
    }

    @Test
    void getCommonFriends_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        String secondLogin = "secondlogin";
        user.setLogin(secondLogin);
        Long secondId = userStorage.createUser(user).getId();
        String thirdLogin = "thirdlogin";
        user.setLogin(thirdLogin);
        Long thirdId = userStorage.createUser(user).getId();
        //when
        userStorage.addFriend(id, secondId);
        userStorage.addFriend(id, thirdId);
        userStorage.addFriend(secondId, thirdId);
        Set<User> friends = (Set<User>) userStorage.getCommonFriends(id, secondId);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getCommonFriends_success_noCommonFriends() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        String secondLogin = "secondlogin";
        user.setLogin(secondLogin);
        Long secondId = userStorage.createUser(user).getId();
        //when
        Set<User> friends = (Set<User>) userStorage.getCommonFriends(id, secondId);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void getFriends_success() throws EntityAlreadyExistException {
        //given
        Long id = userStorage.createUser(user).getId();
        String secondLogin = "secondlogin";
        user.setLogin(secondLogin);
        Long secondId = userStorage.createUser(user).getId();
        //when
        userStorage.addFriend(id, secondId);
        Set<User> friends = (Set<User>) userStorage.getFriends(id);
        //then
        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getFriends_failure_wrongId() throws EntityAlreadyExistException {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                userStorage.getFriends(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found");
    }
}
