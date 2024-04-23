package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
public interface FilmStorage extends LikeStorage {
    Film create(Film film);

    Film update(Film film);

    Collection<Film> getAll();

    String delete(int id);

    Film getById(Integer id);

}
