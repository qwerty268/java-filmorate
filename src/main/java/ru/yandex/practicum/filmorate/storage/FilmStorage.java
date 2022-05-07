package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public void addFilm(Film film);

    public void updateFilm(Film film);

    public List<Film> getFilms();

    Film getFilmById(Long id);
}
