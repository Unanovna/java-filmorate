package storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.db.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@ContextConfiguration(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private Film film;
    private User user;
    private FilmDbStorage filmStorage;
    private UserDbStorage userStorage;

    @BeforeEach
    public void init() {
        film = new Film();
        film.setName("Horror2000");
        film.setDescription("it is terrifying");
        film.setReleaseDate(LocalDate.of(2010, 1, 1));
        film.setDuration(201L);
        film.setMpa(new RatingMpa(1, "G"));
        user = new User();
        user.setEmail("kkkker@eew.ru");
        user.setLogin("va3fefef3");
        user.setName("Ken Ben");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage = new UserDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, userStorage);
    }

    @Test
    void getFilms_success() throws EntityAlreadyExistException {
        //given
        filmStorage.create(film);
        //when
        Collection<Film> allFilms = filmStorage.getAll();
        //then
        assertThat(allFilms)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getFilms_success_noFilms() throws EntityAlreadyExistException {
        //when
        Collection<Film> allFilms = filmStorage.getAll();
        //then
        assertThat(allFilms)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void addFilm_success() throws EntityAlreadyExistException {
        Collection<Film> allFilms = filmStorage.getAll();
        //then
        assertThat(allFilms)
                .isNotNull()
                .hasSize(0);
    }

//    @Test
//    void addFilm_failure() throws EntityAlreadyExistException {
//        //given
//        Film newFilm = new Film();
//        newFilm.setName(film.getName());
//        newFilm.setDescription(film.getDescription());
//        newFilm.setReleaseDate(film.getReleaseDate());
//        newFilm.setDuration(film.getDuration());
//        newFilm.setMpa(film.getMpa());
//        newFilm.setGenres(film.getGenres());
//        //when
//        filmStorage.create(film);
//        //then
//        assertThatThrownBy(() ->
//                filmStorage.create(newFilm))
//                .isInstanceOf(EntityAlreadyExistException.class)
//                .hasMessageContaining("film already exists");
//    }

    @Test
    void updateFilm_success() throws EntityAlreadyExistException {
        //given
        Film newFilm = new Film();
        newFilm.setName(film.getName());
        newFilm.setReleaseDate(film.getReleaseDate());
        newFilm.setDuration(film.getDuration());
        newFilm.setMpa(film.getMpa());
        newFilm.setGenres(film.getGenres());
        Long id = filmStorage.create(film).getId();
        //when
        String newDescription = "not much";
        newFilm.setDescription(newDescription);
        newFilm.setId(id);
        Film updatedFilm = filmStorage.update(newFilm);
        //then
        assertThat(updatedFilm)
                .isNotNull()
                .isNotEqualTo(film);
        assertThat(updatedFilm.getId())
                .isNotNull()
                .isEqualTo(film.getId());
    }

    @Test
    void updateFilm_failure_wrongId() throws EntityAlreadyExistException {
        //given
        Film newFilm = new Film();
        newFilm.setName(film.getName());
        newFilm.setReleaseDate(film.getReleaseDate());
        newFilm.setDuration(film.getDuration());
        newFilm.setMpa(film.getMpa());
        //when
        String newDescription = "not much";
        newFilm.setDescription(newDescription);
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.update(newFilm))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Movie with ID = null not found");
    }

    @Test
    void getFilm_success() throws EntityAlreadyExistException {
        //given
        Long id = filmStorage.create(film).getId();
        //when
        Film storedFilm = filmStorage.getById(id);
        //then
        assertThat(storedFilm)
                .isNotNull()
                .isEqualTo(film);
    }

    @Test
    void getFilm_failure_wrongId() throws EntityAlreadyExistException {
        //when
        Long id = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Movie with ID = 999 not found");
    }

    @Test
    void addLike_success() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.createUser(user).getId();
        Long filmId = filmStorage.create(film).getId();
        //when
        filmStorage.addLike(filmId, userId);
        Film likedFilm = filmStorage.getById(filmId);
        //then
        assertThat(likedFilm.getLikesCount())
                .isNotNull()
                .isEqualTo(1);
    }

    @Test
    void addLike_failure_wrongUserId() throws EntityAlreadyExistException {
        //given
        Long filmId = filmStorage.create(film).getId();
        //when
        Long wrongUserId = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.addLike(filmId, wrongUserId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void addLike_failure_wrongFilmId() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.createUser(user).getId();
        //when
        Long wrongFilmId = 999L;
        //then
        assertThatThrownBy(() ->
                filmStorage.addLike(wrongFilmId, userId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void removeLike_success() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.createUser(user).getId();
        Long filmId = filmStorage.create(film).getId();
        filmStorage.addLike(filmId, userId);
        Film likedFilm = filmStorage.getById(filmId);
        //when
        filmStorage.deleteLike(filmId, userId);
        Film unlikedFilm = filmStorage.getById(filmId);
        //then
        assertThat(likedFilm.getLikesCount())
                .isNotNull()
                .isEqualTo(1);
        assertThat(unlikedFilm.getLikesCount())
                .isNotNull()
                .isEqualTo(0);
    }

//    @Test
//    void removeLike_failure_wrongUserId() throws EntityAlreadyExistException {
//        //given
//        Long filmId = filmStorage.create(film).getId();
//        //when
//        Long wrongUserId = 999L;
//        //then
//        assertThatThrownBy(() ->
//                filmStorage.deleteLike(filmId, wrongUserId))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessageContaining("user not found");
//    }

//    @Test
//    void removeLike_failure_wrongFilmId() throws EntityAlreadyExistException {
//        //given
//        Long userId = userStorage.createUser(user).getId();
//        //when
//        Long wrongFilmId = 999L;
//        //then
//        assertThatThrownBy(() ->
//                filmStorage.deleteLike(wrongFilmId, userId))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessageContaining("film not found");
//    }

    @Test
    void getTopFilms_success() throws EntityAlreadyExistException {
        //given
        Long userId = userStorage.createUser(user).getId();
        Long filmId = filmStorage.create(film).getId();
        filmStorage.addLike(filmId, userId);
        //when
        List<Film> topFilms = filmStorage.getPopular(1, 3L, 1998);
        //then
        assertThat(topFilms)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getTopFilms_success_withNoLikes() throws EntityAlreadyExistException {
        //given
        filmStorage.create(film);
        //when
        List<Film> topFilms = filmStorage.getPopular(1, 3L, 1998);
        //then
        assertThat(topFilms)
                .isNotNull()
                .hasSize(1);
    }
}
