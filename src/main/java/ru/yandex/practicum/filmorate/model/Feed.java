package ru.yandex.practicum.filmorate.model;

import jdk.jfr.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@ToString
public class Feed {
    @NotNull
    private Long timestamp;

    @NotNull
    private Long userId;

    @NotNull
    private EventType eventType;

    @NotNull
    private OperationType operation;

    private Long eventId;

    @NotNull
    private Long entityId;
}