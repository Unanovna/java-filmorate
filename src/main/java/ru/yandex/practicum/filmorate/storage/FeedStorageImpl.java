package ru.yandex.practicum.filmorate.storage;

import jdk.jfr.EventType;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class FeedStorageImpl implements FeedStorage {
    @Override
    public Optional<Feed> addFeed(Long userId, EventType eventType, OperationType operation, Long entityId) {
        return Optional.empty();
    }

    @Override
    public List<Feed> getAllFeedByUserId(Long userId) {
        return null;
    }

    @Override
    public Optional<Feed> getFeedByTimeStamp(Instant timeStamp) {
        return Optional.empty();
    }
}
