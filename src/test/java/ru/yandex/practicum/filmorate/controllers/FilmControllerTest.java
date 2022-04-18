package ru.yandex.practicum.filmorate.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController controller;
    private  Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        controller = new FilmController();

        StringBuilder builder = new StringBuilder();
        builder.setLength(300);

        film2 = Film.builder()
                .dateOfRelease(LocalDate.of(2020, 11, 17))
                .description(builder.toString())
                .id(2)
                .duration(Duration.ofHours(1))
                .name("filmForTest2")
                .build();


        film1 = Film.builder()
                .dateOfRelease(LocalDate.of(2020, 11, 17))
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


        ValidationException exception = assertThrows(ValidationException.class, () ->  controller.addFilm(film2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для Film");


        film2 = film2.toBuilder()
                .description("filmForTest2")
                .dateOfRelease(LocalDate.of(1700, 11, 17))
                .build();

        exception = assertThrows(ValidationException.class, () ->  controller.addFilm(film2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для Film");


        film2 = film2.toBuilder()
                .dateOfRelease(LocalDate.of(2020, 11, 17))
                .duration(Duration.ofHours(-1))
                .build();

        exception = assertThrows(ValidationException.class, () ->  controller.addFilm(film2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для Film");


        film2 = film2.toBuilder()
                .duration(Duration.ofHours(1))
                .name("")
                .build();

        exception = assertThrows(ValidationException.class, () ->  controller.addFilm(film2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для Film");

        film2 = film2.toBuilder()
                .name("film2")
                .build();

        controller.addFilm(film2);
        assertEquals(List.of(film2, film1), controller.getFilms());
    }

    @Test
    void putFilmTest() {
        controller.addFilm(film1);
        controller.putFilm(film1);
        assertEquals(List.of(film1), controller.getFilms());

        ValidationException exception = assertThrows(ValidationException.class, () ->  controller.putFilm(film2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для Film");
    }

    @Test
    void getFilmTest() {
        controller.addFilm(film1);
        assertEquals(List.of(film1), controller.getFilms());
    }
}