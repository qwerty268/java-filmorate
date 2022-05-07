package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping("/films")
    public void addFilm(@RequestBody Film film) {
        service.addFilm(film);
    }

    @PutMapping("/films")
    public void updateFilm(@RequestBody Film film) {
        service.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return service.getFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void putLike(@PathVariable Long id, @PathVariable Long userId) {
        service.putLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        service.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostLikedFilms(@RequestParam Optional<Integer> count) {
        int size = count.orElse(0);
        return service.getMostLikedFilms(size);
    }

}
