package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
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

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film != null) {
            if (userStorage.getById(userId) != null) {
                filmStorage.addLike(filmId, userId);
                log.info("Лайк успешно добавлено");
            } else {
                throw new NotFoundException("Пользователь с ID = " + userId + " не найден");
            }
        } else {
            throw new NotFoundException("Фильм с ID = " + filmId + " не найден");
        }
    }

    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film != null) {
            if (userStorage.getById(userId) != null) {
                filmStorage.deleteLike(filmId, userId);
                log.info("Like successfully removed");
            } else {
                throw new NotFoundException("User with ID = " + userId + " not found");
            }
        } else {
            throw new NotFoundException("Movie with ID = " + filmId + " not found");
        }
        return film;
    }

    public List<Film> getPopular(Integer count, Integer genreId, Integer year) {
        List<Film> result = new ArrayList<>(filmStorage.getPopular(count));
        log.info("Запросил список популярных фильмов");
        return result;
    }

    protected void validate(Film film, String message) {
        if (film.getDescription().length() > LIMIT_LENGTH_OF_DESCRIPTION || film.getReleaseDate().isBefore(LIMIT_DATE)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }
}
