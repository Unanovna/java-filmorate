package ru.yandex.practicum.filmorate.service;

import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
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
    private final UserStorage userDbStorage;

    public Optional<Feed> addEvent(Long userId, EventType eventType, OperationType operationType, Long filmId) {
            Optional<Feed> feed = feedStorage.addEvent(userId, eventType, operationType, filmId);
            if (feed.isEmpty()) {
                throw new RuntimeException("Произошла ошибка при добавлении события");
            }
            log.info("канал добавлен {}", feed.get());
            return Optional.of(feed.get());
        }

    @SneakyThrows
    public Collection<Event> getFeedById(Long userId) {
        userDbStorage.isExist(userId);
        return feedStorage.getFeedById(userId);
    }
}
