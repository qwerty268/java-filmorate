package ru.yandex.practicum.filmorate.storage;

import org.hibernate.sql.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;

import java.lang.ref.PhantomReference;
import java.util.List;
import java.util.Optional;

public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) {
        String sqlQuery = "INSERT INTO Film" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().getSeconds(),
                film.getMPARating());

        updateLikes(film);
    }

    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "UPDATE Film SET " +
                " id = ?," +
                "name = ?," +
                "description = ?," +
                "release_day = ?," +
                "duration = ?," +
                "mpa_rating = ?";

        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().getSeconds(),
                film.getMPARating());

        updateLikes(film);
    }

    @Override
    public List<Film> getFilms() {
        return null;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.empty();
    }

    private void updateLikes(Film film) {
        String sqlQuery = "DELETE FROM Like WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        String likeInSqlQuery = "INSERT INTO Like (?, ?)";

        film.getLikes().forEach((userId) ->
                jdbcTemplate.update(likeInSqlQuery, film.getId(), userId)
        );
    }
}
