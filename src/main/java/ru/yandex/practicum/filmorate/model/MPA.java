package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class  MPA {
   private Integer id;

    public MPA(int id) {
        this.id = id;
    }

    public MPA() {
    }

}
