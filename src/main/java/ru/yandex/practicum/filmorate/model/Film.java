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
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
    Set<Long> likes;

    public void putLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }
}

