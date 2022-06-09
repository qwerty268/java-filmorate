package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private List<String> genre;
    private MPA mpa;


    public Film(long id, String name, String description, LocalDate releaseDate, Duration duration, Set<Long> likes, List<String> genre, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;

        this.genre = genre;

        this.mpa = mpa;
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

    //для тестов
    public boolean everyFieldEquals(Film film) {
        return this.id == film.getId()
                && Objects.equals(this.name, film.getName())
                && Objects.equals(this.description, film.getDescription())
                && this.releaseDate.equals(film.getReleaseDate())
                && this.duration.equals(film.getDuration())
                && this.likes.equals(film.getLikes())
                && this.mpa.equals(film.getMpa())
                && this.mpa.equals(film.mpa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

