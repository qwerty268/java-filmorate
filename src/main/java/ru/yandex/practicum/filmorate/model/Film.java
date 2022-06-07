package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* анотация @Value делает все поля приватными и финальными, а также добавляет геттеры.
 Изменение полей идет через копирование объекта*/
@Data
@Builder(toBuilder = true)
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Long> likes;
    private List<Genre> genre;
    private MPA rating;


    public Film(long id, String name, String description, LocalDate releaseDate, Duration duration, Set<Long> likes, List<Genre> genre, MPA rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;

        this.genre = genre;

        this.rating = rating;
    }

    public void putLike(Long userId) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    public boolean everyFieldEquals(Film film) {
        return this.id == film.getId()
                && Objects.equals(this.name, film.getName())
                && Objects.equals(this.description, film.getDescription())
                && this.releaseDate.equals(film.getReleaseDate())
                && this.duration.equals(film.getDuration())
                && this.likes.equals(film.getLikes())
                && this.rating.equals(film.getRating())
                && this.rating.equals(film.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

