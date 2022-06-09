package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Service
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();


    @Override
    public void addUser(User user) {

        users.put(user.getId(), user);
        log.debug("Добавлен новый User: {}", user);

    }

    @Override
    public void updateUser(User user) {


        users.put(user.getId(), user);
        log.debug("User {} обновлен", user);

    }

    @Override
    public List<User> getUsers() {
        List<User> listOfUsers = new ArrayList<>(users.values());
        return listOfUsers;
    }

    @Override
    public Optional<User> getUserById(java.lang.Long id) {
        return Optional.ofNullable(users.get(id));
    }



}
