package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public interface GenreStorage {

    void deleteAllGenresById(int filmId);

    Genre getGenreById(int genreId);

    List<Genre> getAllGenres();
}
