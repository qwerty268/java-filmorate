package ru.yandex.practicum.filmorate.exceptions;

public class ValidationErrorException extends RuntimeException {
    public ValidationErrorException(String message) {
        super(message);
    }
}
