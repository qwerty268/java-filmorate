package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;
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
    private Genre genre;

    public void putLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }
}

enum Genre {
    COMEDY,
    DRAMA,
    THRILLER,
    DOCUMENTARY,
    ACTION,
    CARTOON
}

enum MPA {
    G,
    PG,
    PG13,
    R,
    NC17
}