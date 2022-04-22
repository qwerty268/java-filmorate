package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;

/* анотация @Value делает все поля приватными и финальными, а также добавляет геттеры.
 Изменение полей идет через копирование объекта*/
@Value
@Builder(toBuilder = true)
public class Film {
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
}
