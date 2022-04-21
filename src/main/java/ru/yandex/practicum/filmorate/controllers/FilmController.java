package ru.yandex.practicum.filmorate.controllers;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {

    private final Map<String, Film> films = new HashMap<>();


    @PostMapping("/films")
    public void addFilm(@RequestBody Film film) {
        if (filter(film)) {
            films.put(film.getName(), film);
        }
        log.debug("Добавлн новый фильм: {}", film);
    }

    @PutMapping("/films")
    public void updateFilm(@RequestBody Film film) {
        if (filter(film)) {
            films.put(film.getName(), film);
        }
        log.debug("Обновлен {}", film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        List<Film> listOfFilms = new ArrayList<>();
        listOfFilms.addAll(films.values());

        return listOfFilms;
    }

    private boolean filter(Film film) {
        if (film.getName().isBlank()
                || film.getDescription().length() > 200
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))
                || film.getDuration().isNegative()) {
            log.error("Валидация не пройдена (Film)");
            throw new ValidationException("Переданы ошибочные данные для Film");
        }
        return true;
    }
}
