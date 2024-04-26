package ru.yandex.practicum.filmorate.service;

import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {
    private final FeedStorage feedStorage;
    private final UserStorage userStorage;

    public Optional<Feed> addEvent(Long userId, ru.yandex.practicum.filmorate.model.EventType eventType, OperationType operationType, Long filmId) {
            Optional<Feed> feed = feedStorage.addEvent(userId, eventType, operationType, filmId);
            if (feed.isEmpty()) {
                throw new RuntimeException("Произошла ошибка при добавлении события");
            }
            log.info("канал добавлен {}", feed.get());
            return Optional.of(feed.get());
        }

    private void containsUser(Integer userId) {
        if (!userStorage.containsUser(userId)) {
            throw new NotFoundException(String.format("Пользователь по идентификатору '%d' не найден", userId));
        }
    }

    @SneakyThrows
    public Collection<Event> getFeedById(Long userId) {
        userStorage.isExist(userId);
        return feedStorage.getFeedById(userId);
    }
}
