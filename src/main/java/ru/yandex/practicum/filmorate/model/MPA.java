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

    @Override
    public String toString() {
        switch (id) {
            case 1: return "G";
            case 2: return "PG";
            case 3: return "PG13";
            case 4:return  "R";
            case 5:return "NC17";
        }

        return null;
    }

    public static MPA valueOf(String s) {
        switch (s) {
            case "G": return new MPA(1);
            case "PG": return new MPA(2);
            case "PG13": return new MPA(3);
            case "R": return new MPA(4);
            case "NC": return new MPA(5);
        }
        return null;
    }
}
