package ru.yandex.practicum.filmorate.exceptions;

public class FilmDoesNotExistException extends RuntimeException {
    public FilmDoesNotExistException(Long id) {
        super("Фильм с id=" + id + " не существет.");
    }
}
