package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;
    private static final LocalDate LIMIT_DATE = LocalDate.from(LocalDateTime.of(1895, 12, 28, 0, 0));
    private static final int LIMIT_LENGTH_OF_DESCRIPTION = 200;

    public Collection<Film> getAll() {
        log.info("Список всех фильмов: " + filmStorage.getAll().size());
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        validate(film, "Форма фильма заполнена неверно");
        Film result = filmStorage.create(film);
        log.info("Фильм успешно добавлен: " + film);
        return result;
    }

    public Film update(Film film) {
        validate(film, "Форма обновления фильма заполнена неверно");
        Film result = filmStorage.update(film);
        log.info("Фильм успешно обновлен" + film);
        return result;
    }

    public void delete(Long filmId) {
        if (getById(filmId) == null) {
            throw new NotFoundException("Фильм с ID = " + filmId + " не найден");
        }
        log.info("Фильм с id: {}", filmId);
        filmStorage.deleteFilmById(filmId);
    }

    public Film getById(Long id) {
        log.info("Запрошенный пользователь с ID = " + id);
        return filmStorage.getById(id);
    }

    @SneakyThrows
    public Film addLike(Long filmId, Long userId) {
        filmStorage.isExist(filmId);
        userStorage.isExist(userId);
        feedStorage.addEvent(userId, EventType.LIKE, OperationType.ADD, filmId);
        return filmStorage.addLike(filmId, userId);
    }

    @SneakyThrows
    public Film deleteLike(Long filmId, Long userId) {
        filmStorage.isExist(filmId);
        userStorage.isExist(userId);
        feedStorage.addEvent(userId, EventType.LIKE, OperationType.REMOVE, filmId);
        return filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopular(Integer count, Long genreId, Integer year) {
        return filmStorage.getPopular(count, genreId, year);
    }

    public List<Film> getFriendsCommonFilms(Long userId, Long friendId) {
        return filmStorage.getFriendsCommonFilms(userId, friendId);
    }

    protected void validate(Film film, String message) {
        if (film.getDescription().length() > LIMIT_LENGTH_OF_DESCRIPTION || film.getReleaseDate().isBefore(LIMIT_DATE)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }
}
