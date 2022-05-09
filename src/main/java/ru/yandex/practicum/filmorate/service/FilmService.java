package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage;

    @Autowired
    public FilmService(UserStorage userStorage) {
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
        return filmStorage.getFilmById(id);
    }


    public void putLike(Long filmId, Long userId) {
        checkingForNotNullValues(filmId, userId);

        filmStorage.getFilmById(filmId).putLike(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        checkingForNotNullValues(filmId, userId);

        filmStorage.getFilmById(filmId).removeLike(userId);
    }

    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = filmStorage.getFilms();
        films.sort(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());

        if (count == 0) {
            if (films.size() < 10) {
                return films;
            }
            films = films.subList(0, 9);
        } else {
            films = films.subList(0, count);
        }
        return films;
    }

    private void checkingForNotNullValues(Long filmId, Long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserDoesNotExistException(userId);
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new FilmDoesNotExistException(filmId);
        }
    }
}
