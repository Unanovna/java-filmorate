package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:WhitespaceAround")
public class InMemoryGenreStorage implements GenreStorage{
    @Override
    public void deleteAllGenresById(int filmId) {
    }

    @Override
    public Genre getGenreById(int genreId) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }
}
