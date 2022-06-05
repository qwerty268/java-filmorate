package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component("FilmDbStorage")
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
                film.getMPARatingSQL());

        updateGenres(film);
        updateLikes(film);
    }

    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "UPDATE Film SET " +
                " id = ?," +
                "name = ?," +
                "description = ?," +
                "release_date = ?," +
                "duration = ?," +
                "mpa_rating = ?";

        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().getSeconds(),
                film.getMPARatingSQL());

        updateGenres(film);
        updateLikes(film);
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT * FROM Film";

        return jdbcTemplate.query(sqlQuery, this::FilmFromSQL);
    }


    private MPA getMPAFromSQL(Integer ratingId) {
        String sqlQuery = "SELECT rating_value FROM mpa_rating WHERE mpa_rating_id = ? ";

        return jdbcTemplate.queryForObject(sqlQuery,
                (rs, rowNum) -> MPA.valueOf(rs.getString("rating_value")), ratingId);
    }

    private List<Genre> getGenreFromSQL(Integer filmId) {
        String sqlQuery = "SELECT g.genre FROM Film_genre AS fg " +
                " LEFT JOIN Genre AS g ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> Genre.valueOf(rs.getString("genre")), filmId);
    }

    private Set<Long> getLikesFromSQL(Integer filmId) {
        String sqlQuery = "SELECT user_id FROM Likes WHERE film_id = ?";

        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("user_id"), filmId));
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        String  sqlQuert = "SELECT * FROM Film WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuert, this::FilmFromSQL, id));
    }

    private void updateLikes(Film film) {
        String sqlQuery = "DELETE FROM Likes WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        String likeInSqlQuery = "INSERT INTO Likes VALUES (?, ?)";

        if (film.getLikes() != null) {
            film.getLikes().forEach((userId) ->
                    jdbcTemplate.update(likeInSqlQuery, film.getId(), userId)
            );
        }
    }

    private void updateGenres(Film film) {
        String sqlQuery = "DELETE FROM Film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        String genreInSqlQuery = "INSERT INTO Film_genre VALUES (?, ?)";

        if (film.getGenre() != null) {
            film.getGenre().forEach((genre) ->
                    jdbcTemplate.update(genreInSqlQuery, film.getId(), GenreToSQL(genre))
            );
        }
    }

    private Integer GenreToSQL(Genre genre) {
        String sqlQuery = "SELECT genre_id FROM Genre WHERE genre = ?";

        return jdbcTemplate.queryForObject(sqlQuery,
                (rs, rowNum) -> rs.getInt("genre_id"), genre.toString());
    }
    private Film FilmFromSQL(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Duration duration = Duration.ofSeconds(rs.getInt("duration"));

        int mpaRatingId = rs.getInt("mpa_rating");

        MPA mpaRating = getMPAFromSQL(mpaRatingId);
        List<Genre> genre = getGenreFromSQL(id);
        Set<Long> likes = getLikesFromSQL(id);

        return new Film(id, name, description, releaseDate, duration, likes, genre, mpaRating);
    }
}
