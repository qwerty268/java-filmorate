package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate ;
@Autowired
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
        String sqlQuery = "SELECT * FROM film";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate =  rs.getDate("release_date").toLocalDate();
        Duration duration = Duration.ofSeconds(rs.getInt("duration"));
        int mpaRatingId = rs.getInt("mpa_rating");

        String mpaRating = makeMPARating(mpaRatingId);

        return new Film(id, name, description, releaseDate, mpaRating);
    }

    private String makeMPARating(Integer ratingId) {
        String sqlQuery = "SELECT rating_value FROM mpa_rating WHERE mpa_rating_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) ->  rs.getString("rating_value"), ratingId).get(0);
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
