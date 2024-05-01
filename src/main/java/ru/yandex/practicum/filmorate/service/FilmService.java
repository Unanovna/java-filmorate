package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userDbStorage;
    private final FilmStorage filmDbStorage;
    private final FeedStorage feedStorage;
    private static final LocalDate LIMIT_DATE = LocalDate.from(LocalDateTime.of(1895, 12, 28, 0, 0));
    private static final int LIMIT_LENGTH_OF_DESCRIPTION = 200;

    public Collection<Film> getAll() {
        log.info("Список всех фильмов: " + filmDbStorage.getAll().size());
        return filmDbStorage.getAll();
    }

    public Film create(Film film) {
        validate(film, "Форма фильма заполнена неверно");
        Film result = filmDbStorage.create(film);
        log.info("Фильм успешно добавлен: " + film);
        return result;
    }

    public Film update(Film film) {
        validate(film, "Форма обновления фильма заполнена неверно");
        Film result = filmDbStorage.update(film);
        log.info("Фильм успешно обновлен" + film);
        return result;
    }

    public String deleteFilmById(Long filmId) {
        return filmDbStorage.deleteFilmById(filmId);
    }

    public Film getById(Long id) {
        log.info("Запрошенный пользователь с ID = " + id);
        return filmDbStorage.getById(id);
    }

    @SneakyThrows
    public Film addLike(Long filmId, Long userId) {
        filmDbStorage.isExist(filmId);
        userDbStorage.isExist(userId);
        feedStorage.addEvent(userId, EventType.LIKE, OperationType.ADD, filmId);
        return filmDbStorage.addLike(filmId, userId);
    }

    @SneakyThrows
    public Film deleteLike(Long filmId, Long userId) {
        filmDbStorage.isExist(filmId);
        userDbStorage.isExist(userId);
        feedStorage.addEvent(userId, EventType.LIKE, OperationType.REMOVE, filmId);
        return filmDbStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopular(Integer count, Long genreId, Integer year) {
        return filmDbStorage.getPopular(count, genreId, year);
    }

    protected void validate(Film film, String message) {
        if (film.getDescription().length() > LIMIT_LENGTH_OF_DESCRIPTION || film.getReleaseDate().isBefore(LIMIT_DATE)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }
}
