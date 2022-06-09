package ru.yandex.practicum.filmorate.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
        super("Неверный id");
    }
}
