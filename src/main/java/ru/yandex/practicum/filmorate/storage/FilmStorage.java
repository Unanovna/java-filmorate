package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
public interface FilmStorage extends LikeStorage {
    Film create(Film film);
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    Film update(Film film);
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    Collection<Film> getAll();
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    String delete(int id);

    Film getById(Integer id);

}
