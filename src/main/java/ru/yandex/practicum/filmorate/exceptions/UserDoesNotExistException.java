package ru.yandex.practicum.filmorate.exceptions;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(Long id) {
        super("Пользователь с id=" + id + " не существует.");
    }
}
