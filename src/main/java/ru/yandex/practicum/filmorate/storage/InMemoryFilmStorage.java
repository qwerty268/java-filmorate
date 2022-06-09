package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationErrorException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public void addFilm(@RequestBody Film film) {

        films.put(film.getId(), film);
        log.debug("Добавлн новый фильм: {}", film);
    }

    @Override
    public void updateFilm(@RequestBody Film film) {
        films.put(film.getId(), film);
        log.debug("Обновлен {}", film);
    }

    @Override
    public List<Film> getFilms() {
        List<Film> listOfFilms = new ArrayList<>(films.values());
        return listOfFilms;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.ofNullable(films.get(id));
    }


}
