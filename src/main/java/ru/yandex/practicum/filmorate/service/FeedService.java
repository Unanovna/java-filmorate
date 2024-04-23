package ru.yandex.practicum.filmorate.service;

import jdk.jfr.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

    @Service
    @Slf4j
    public class FeedService {
        private final FeedStorage feedStorage;
        private final UserStorage userStorage;

        @Autowired
        public FeedService(FeedStorage feedStorage, UserStorage userStorage) {
            this.feedStorage = feedStorage;
            this.userStorage = userStorage;
        }

        public Feed addFeed(Long userId, EventType eventType, OperationType operation, Long entityId) {
            Optional<Feed> feed = feedStorage.addFeed(userId, eventType, operation, entityId);
            if (feed.isEmpty()) {
                throw new RuntimeException("Произошла ошибка при добавлении события");
            }
            log.info("канал добавлен {}", feed.get());
            return feed.get();
        }

        public List<Feed> getAllFeedByUserId(Long userId) {
            containsUser(userId.intValue());
            List<Feed> feed = feedStorage.getAllFeedByUserId(userId);
            log.info("Весь канал пользователя {} возвращается.", userId);
            return feed;
        }

        private void containsUser(Integer userId) {
            if (!userStorage.containsUser(userId)) {
                throw new NotFoundException(String.format("Пользователь по идентификатору '%d' не найден", userId));
            }
        }

        public void addFeed(Long userId, ru.yandex.practicum.filmorate.model.EventType eventType, OperationType operationType, Long entityId) {
        }
    }
