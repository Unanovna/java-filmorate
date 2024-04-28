package ru.yandex.practicum.filmorate.model;

import jdk.jfr.EventType;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@Data
public class Feed {
    Long timestamp;
    Long userId;
    EventType eventType;
    OperationType operation;
    Long eventId;
    Long entityId;
}