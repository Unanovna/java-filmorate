package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@RestController
public class FilmController {
    @SuppressWarnings("checkstyle:MemberName")
    @Getter
    Map<Integer, Film> films = new HashMap<>();
    @Getter
    private static Integer idController = 1;

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        validate(film);
        film.setId(idController);
        if (films.containsValue(film)) {
            log.trace("Данный Фильм уже содержится в рейтинге");
            throw new ValidateException("Данный Фильм уже содержится в рейтинге");
        }
        films.put(film.getId(), film);
        generateId();
        return film;
    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.trace("Обновление невозможно - фильм с указанным id " + film.getId() + " отсутствует в рейтинге");
            throw new ValidateException("Обновление невозможно - фильм с указанным id " + film.getId() + " отсутствует в рейтинге");
        }
        return film;
    }

    @GetMapping("/films")
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Integer generateId() {
        idController = films.size() + 1;
        return idController;
    }

    public void validate(Film film) {
        if (film.getDuration() == null || film.getDuration() < 0) {
            log.trace("Продолжительность должна быть положительной");
            throw new ValidateException("Продолжительность должна быть положительной");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.trace("Имя фильма не может быть пустым");
            throw new ValidateException("Имя фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.trace("максимальная длина описания — 200 символов");
            throw new ValidateException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null) {
            log.trace("Дата релиза должна быть заполнена");
            throw new ValidateException("Дата релиза должна быть заполнена");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.trace("дата релиза — не раньше 28 декабря 1895");
            throw new ValidateException("дата релиза — не раньше 28 декабря 1895");
        }
    }
}