package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/* анотация @Value делает все поля приватными и финальными, а также добавляет геттеры.
 Изменение полей идет через копирование объекта*/
@Value
@Builder(toBuilder = true)
public class User {
    long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
