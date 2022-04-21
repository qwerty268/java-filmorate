package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();


    @PostMapping("/films")
    public void addFilm(@RequestBody Film film) {
        if (filter(film)) {
            films.put(film.getId(), film);
        }
        log.debug("Добавлн новый фильм: {}", film);
    }

    @PutMapping("/films")
    public void updateFilm(@RequestBody Film film) {
        if (filter(film)) {
            films.put(film.getId(), film);
        }
        log.debug("Обновлен {}", film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        List<Film> listOfFilms = new ArrayList<>(films.values());
        return listOfFilms;
    }

    private boolean filter(Film film) {
        StringBuilder builder = new StringBuilder();
        if (film.getName().isBlank()) {
            builder.append(" Передано пустое имя;");
        }

        if (film.getDescription().length() > 200) {
            builder.append(" Описание содержит больше, чем 200 символов;");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))) {
            builder.append(" Дата релиза введена некорректно;");
        }

        if (film.getDuration().isNegative()) {
            builder.append(" Введена отрицательная продолжительность фильма.");
        }


        String cause = builder.toString();
        if (!cause.isBlank()) {
            cause = builder.replace(builder.length() - 1, builder.length(), ".").toString();
            log.error("Валидация не пройдена (Film):" + cause);
            throw new ValidationException("Переданы ошибочные данные для Film:" + cause);
        }
        return true;
    }
}
