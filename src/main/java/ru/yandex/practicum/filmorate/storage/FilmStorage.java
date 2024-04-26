package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Component
public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Collection<Film> getAll();

    Film getById(Long id);

    List<Film> getPopular(Integer count, Long genreId, Integer year);

    Film addLike(Long filmId, Long userId);

    void isExist(Long filmId);

    void validateFilm(Film film);

    String deleteFilmById(Long filmId);

    Film deleteLike(Long filmId, Long userId);

    List<Film> getFriendsCommonFilms(Long userId, Long friendId);
}
