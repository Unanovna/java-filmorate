package ru.yandex.practicum.filmorate.exception;

public class EntityAlreadyExistException extends Exception {
    public EntityAlreadyExistException() {
        super();
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
