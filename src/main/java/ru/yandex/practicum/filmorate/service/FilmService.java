package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private static Long filmId = 1L;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addFilm(Film film) {
        filter(film);
        createId(film);
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        filter(film);
        getFilmById(film.getId());
        filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        if (id < 0) {
            throw new InvalidIdException();
        }
        return filmStorage.getFilmById(id).orElseThrow(() -> new FilmDoesNotExistException(id));
    }


    public void putLike(Long filmId, Long userId) {
        userStorage.getUserById(userId).orElseThrow(() -> new UserDoesNotExistException(userId));

        Film film = filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmDoesNotExistException(filmId));
        film.putLike(userId);
        updateFilm(film);
    }

    public void removeLike(Long filmId, Long userId) {
        userStorage.getUserById(userId).orElseThrow(() ->
                    new UserDoesNotExistException(userId)
        );

        Film film = filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmDoesNotExistException(filmId));
        film.removeLike(userId);
        updateFilm(film);
    }

    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = filmStorage.getFilms();
        films.sort(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());

        if (films.size() >= count) {
            films = films.subList(0, count);
        }
        return films;
    }

    private boolean filter(Film film) {
        StringBuilder builder = new StringBuilder();
        if (film.getName().isBlank()) {
            builder.append(" Передано пустое имя;");
        }

        if (film.getDescription().length() > 200) {
            builder.append(" Описание содержит больше, чем 200 символов;");
        }

        if (Objects.equals(film.getDescription(), "")) {
            builder.append(" Описание отсутствует;");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))) {
            builder.append(" Дата релиза введена некорректно;");
        }

        if (film.getDuration().isNegative()) {
            builder.append(" Введена отрицательная продолжительность фильма.");
        }

        if (film.getMpa() == null) {
            builder.append(" MPA не указан.");
        }


        String cause = builder.toString();
        if (!cause.isBlank()) {
            cause = builder.replace(builder.length() - 1, builder.length(), ".").toString();
            log.error("Валидация не пройдена (Film):" + cause);
            throw new ValidationErrorException("Переданы ошибочные данные для Film:" + cause);
        }
        return true;
    }

    private void createId(Film film) {
        if (film.getId() == 0) {
            film.setId(filmId);
            filmId++;
        }
    }
}
