package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public void addFilm(Film film);

    public void updateFilm(Film film);

    public List<Film> getFilms();

    Optional<Film> getFilmById(Long id);
}
