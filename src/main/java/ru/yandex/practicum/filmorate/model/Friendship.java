package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Friendship {
    private final Long user1;
    private final Long user2;
    private Boolean status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2) ||
                Objects.equals(user1, that.user2) && Objects.equals(user2, that.user1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}
