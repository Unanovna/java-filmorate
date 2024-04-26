package ru.yandex.practicum.filmorate.storage;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static Map<Integer, Film> films = new HashMap<>();
    UserStorage userStorage;

    public static Integer idController = 1;
    public static Logger log;
    public static int filmId;

    @Override
    public Film create(Film film) {
        film.setId(idController);
        if (films.containsValue(film)) {
            log.trace("Данный Фильм уже содержится в рейтинге");
            throw new ValidateException("Данный Фильм уже содержится в рейтинге");
        }
        films.put(film.getId(), film);
        generateId();
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.trace("Обновление невозможно - фильм с указанным id " + film.getId() + " отсутствует в рейтинге");
            throw new ValidateException("Обновление невозможно - фильм с указанным id " + film.getId() + " отсутствует в рейтинге");
        }
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(Long filmId) {
        isExist(filmId);
        log.info("Фильм {} возвращен", films.get(filmId));
        return films.get(filmId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        log.info("Возвращено топ {} фильмов", count);
        return getAll().stream()
                .sorted((f1, f2) -> f2.getLikesList().size() - f1.getLikesList().size())
                .limit(count)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Integer generateId() {
        idController = films.size() + 1;
        return idController;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        films.get(filmId).getLikesList().add(userId);
        log.info("Фильму {} поставил лайк пользователь {}", films.get(filmId), userId);
        return films.get(filmId);
    }

    @SneakyThrows
    @Override
    public void isExist(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new ObjectNotFoundException("Фильма с таким " + filmId + " не существует");
        }
    }

    @SneakyThrows
    @Override
    public Film deleteLike(Long filmId, Long userId) {
        isExist(filmId);
        userStorage.isExist(userId);
        films.get(filmId).getLikesList().remove(userId);
        log.info("Фильму {} удалил лайк пользователь {}", films.get(filmId), userId);
        return films.get(filmId);
    }

    public String deleteFilmById(Long filmId) {
        return "Фильм film_id=" + filmId + " успешно удален.";
    }

    @Override
    public Film addFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("Фильм не может быть пустым");
        }
        if (films.containsKey(film.getId())) {
            return update(film);
        } else {
            validateFilm(film);
            film.setId(++filmId);
            films.put(film.getId(), film);
            return film;
        }
    }

    @Override
    public void validateFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("Объект не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        }
    }
}
