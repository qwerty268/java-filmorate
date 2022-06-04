package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    public Integer getMPARatingSQL() {
        switch (rating) {
            case G:
                return 1;
            case PG:
                return 2;
            case PG13:
                return 3;
            case R:
                return 4;
            case NC17:
                return 5;
        }
        return null;
    }

}

