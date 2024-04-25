package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    void addLike(int filmId, Long userId);

    void removeLike(int filmId, Long userId);

    List<Film> getPopular(Integer count);
}
