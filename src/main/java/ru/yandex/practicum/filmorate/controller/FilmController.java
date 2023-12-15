package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@RestController
public class FilmController {
    @SuppressWarnings("checkstyle:MemberName")
    @Getter
    private HashMap<Integer, Film> films = new HashMap<>();
    @Getter
    private static Integer idController = 1;

    //добавление фильма
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

    //обновление фильма
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

    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    @GetMapping("/films")
    public Collection<Film> getAll() {
        return films.values();
    }

    public Integer generateId() {
        idController =  films.size() + 1;
        return idController;
    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    public void validate(Film film) {
        if (film.getDescription().length() > 200) {
            log.trace("максимальная длина описания — 200 символов");
            throw new ValidateException("максимальная длина описания — 200 символов");
        }
        String dateToString = String.valueOf(film.getReleaseDate());
        String [] split = dateToString.split("-");
        Date date = new Date(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        if (date.before(new Date(1895, 12, 25))) {
            log.trace("дата релиза — не раньше 28 декабря 1985");
            throw new ValidateException("дата релиза — не раньше 28 декабря 1985");
        }
    }
}