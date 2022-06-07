package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Friendship {
    private final Long user1; //user, предложивший дружить
    private final Long user2;
    private Boolean status = false;

    public Friendship(Long user1, Long user2, Boolean status) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
    }

    public Friendship(Long user1, Long user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}
