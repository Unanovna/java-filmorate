package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.*;

import static sun.security.util.KeyUtil.validate;
@Component

public class InMemoryFilmStorage implements FilmStorage {

    Map<Integer, Film> films = new HashMap<>();

    private static Integer idController = 1;
    private Logger log;

    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    @Override
    public Film create(Film film) {
        try {
            validate((Key) film);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
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
        try {
            validate((Key) film);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
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
    public String delete(int id) {
        return null;
    }

    @Override
    public Film getById(Integer id) {
        return null;
    }

    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    public Integer generateId() {
        idController = films.size() + 1;
        return idController;
    }

    @Override
    public void addLike(int filmId, int userId) {

    }

    @Override
    public void removeLike(int filmId, int userId) {

    }

    @Override
    public List<Film> getPopular(Integer count) {
        return null;
    }
}
