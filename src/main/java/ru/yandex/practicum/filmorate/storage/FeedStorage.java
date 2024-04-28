package ru.yandex.practicum.filmorate.storage;

import jdk.jfr.Event;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.util.Collection;
import java.util.Optional;

@Component
public interface FeedStorage {

    public Collection<Event> getFeedById(Long userId);

    Optional<Feed> addEvent(Long userId, EventType eventType, OperationType operationType, Long filmId);

}