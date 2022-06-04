package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id).orElseThrow(() -> new FilmDoesNotExistException(id));
    }


    public void putLike(Long filmId, Long userId) {
        userStorage.getUserById(userId).orElseThrow(() -> new UserDoesNotExistException(userId));

        filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmDoesNotExistException(filmId)).putLike(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        userStorage.getUserById(userId).orElseThrow(() -> new UserDoesNotExistException(userId));

        filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmDoesNotExistException(filmId)).removeLike(userId);
    }

    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = filmStorage.getFilms();
        films.sort(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());

        if (films.size() >= count) {
            films = films.subList(0, count);
        }
        return films;
    }

}
