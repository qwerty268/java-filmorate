package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.ValidationErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    @Autowired
    private FilmService controller;
    private Film film1;
    private Film film2;


    @BeforeEach
    public void beforeEach() {

        StringBuilder builder = new StringBuilder();
        builder.setLength(300);

        film2 = Film.builder()
                .releaseDate(LocalDate.of(2020, 11, 17))
                .description(builder.toString())
                .id(2)
                .duration(Duration.ofHours(1))
                .name("filmForTest2")
                .build();


        film1 = Film.builder()
                .releaseDate(LocalDate.of(2020, 11, 17))
                .description("filmForTest1")
                .id(1)
                .duration(Duration.ofHours(1))
                .name("filmForTest1")
                .build();
    }

    @Test
    void addFilmTest() {
        controller.addFilm(film1);
        assertEquals(List.of(film1), controller.getFilms());


        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> controller.addFilm(film2));
        assertEquals(exception.getMessage(),
                "Переданы ошибочные данные для Film: Описание содержит больше, чем 200 символов.");


        film2 = film2.toBuilder()
                .description("filmForTest2")
                .releaseDate(LocalDate.of(1700, 11, 17))
                .build();

        exception = assertThrows(ValidationErrorException.class, () -> controller.addFilm(film2));
        assertEquals(exception.getMessage(),
                "Переданы ошибочные данные для Film: Дата релиза введена некорректно.");


        film2 = film2.toBuilder()
                .releaseDate(LocalDate.of(2020, 11, 17))
                .duration(Duration.ofHours(-1))
                .build();

        exception = assertThrows(ValidationErrorException.class, () -> controller.addFilm(film2));
        assertEquals(exception.getMessage(),
                "Переданы ошибочные данные для Film: Введена отрицательная продолжительность фильма.");


        film2 = film2.toBuilder()
                .duration(Duration.ofHours(1))
                .name("")
                .build();

        exception = assertThrows(ValidationErrorException.class, () -> controller.addFilm(film2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для Film: Передано пустое имя.");

        film2 = film2.toBuilder()
                .name("film2")
                .build();

        controller.addFilm(film2);
        assertEquals(List.of(film1, film2), controller.getFilms());
    }

    @Test
    void putFilmTest() {
        controller.addFilm(film1);
        controller.updateFilm(film1);
        assertEquals(List.of(film1), controller.getFilms());

        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> controller.updateFilm(film2));
        assertEquals(exception.getMessage(),
                "Переданы ошибочные данные для Film: Описание содержит больше, чем 200 символов.");
    }

    @Test
    void getFilmTest() {
        controller.addFilm(film1);
        assertEquals(List.of(film1), controller.getFilms());
    }
}