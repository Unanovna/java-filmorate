package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FeedService feedService;

    @Autowired
    public FilmController(FilmService filmService, FeedService feedService) {
        this.filmService = filmService;
        this.feedService = feedService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        feedService.addEvent(userId, EventType.LIKE, OperationType.ADD, Long.valueOf(id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") Integer count,
                                       @RequestParam(required = false) Long genreId,
                                       @RequestParam(required = false) Integer year) {
        return filmService.getPopular(count, genreId, year);
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