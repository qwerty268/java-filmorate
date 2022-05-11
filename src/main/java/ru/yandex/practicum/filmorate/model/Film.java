package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder(toBuilder = true)
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Long> likes;

    //в тестах передаётся объект User без friends, поэтому приходиться делать проверку notNull
    public void putLike(Long userId) {
        if (likes == null) {
            likes = new HashSet<>();
        }

        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }
}

