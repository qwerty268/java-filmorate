package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/* анотация @Value делает все поля приватными и финальными, а также добавляет геттеры.
 Изменение полей идет через копирование объекта*/
@Data
@Builder(toBuilder = true)
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Friendship> friends;

    public User(long id, String email, String login, String name, LocalDate birthday, Set<Friendship> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;
    }

    public void addFriend(User user) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(new Friendship(this.getId(), user.getId()));
    }

    public void addFriendRequest(User user) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(new Friendship(user.getId(), this.getId()));
    }

    public void deleteFriend(User user) {
        friends.remove(new Friendship(this.getId(), user.getId()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //для тестов
    public boolean everyFieldEquals(User user) {
        if (this.id == user.getId()
                && this.email.equals(user.getEmail())
                && this.getLogin().equals(user.getLogin())
                && this.name.equals(user.getName())
                && this.birthday.equals(user.getBirthday())
                && this.friends.equals(user.getFriends())) {
            return true;
        }

        return false;
    }
}
