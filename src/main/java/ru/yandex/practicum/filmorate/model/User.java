package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder(toBuilder = true)
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

    //в тестах передаётся объект User без friends, поэтому приходиться делать проверку notNull

    public void addFriend(User user) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(user.getId());
    }

    public void deleteFriend(User user) {
        friends.remove(user);
    }
}
