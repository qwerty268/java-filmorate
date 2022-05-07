package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final FilmStorage storage = new InMemoryFilmStorage();

    @PostMapping("/films")
    public void addFilm(@RequestBody Film film) {
        storage.addFilm(film);
    }

    @PutMapping("/films")
    public void updateFilm(@RequestBody Film film) {
        storage.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return storage.getFilms();
    }
}
