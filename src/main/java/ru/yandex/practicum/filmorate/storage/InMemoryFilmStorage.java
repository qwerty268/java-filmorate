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

    private static Long filmId = 1L;

    @Override
    public void addFilm(@RequestBody Film film) {
        if (filter(film)) {
            createId(film);
            films.put(film.getId(), film);
            log.debug("Добавлн новый фильм: {}", film);

            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
        }

    }

    @Override
    public void updateFilm(@RequestBody Film film) {
        if (filter(film)) {
            films.put(film.getId(), film);
            log.debug("Обновлен {}", film);

            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
        }

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

    private boolean filter(Film film) {
        StringBuilder builder = new StringBuilder();
        if (film.getName().isBlank()) {
            builder.append(" Передано пустое имя;");
        }

        if (film.getDescription().length() > 200) {
            builder.append(" Описание содержит больше, чем 200 символов;");
        }

        if (film.getDescription() == "") {
            builder.append(" Описание отсутствует;");
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
