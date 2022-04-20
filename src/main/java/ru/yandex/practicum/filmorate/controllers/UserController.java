package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping("/users")
    public void addUser(@RequestBody User user) {
        if (filter(user)) {
                users.put(user.getId(), user);
        }
        log.debug("Добавлен новый Use: {}", user);
    }

    @PutMapping("/users")
    public void putUser(@RequestBody User user) {

        if (filter(user)) {
                users.put(user.getId(), user);
        }
        log.debug("User {} обновлен", user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.addAll(users.values());
        return listOfUsers;
    }


    private boolean filter(User user) {
        if (user.getEmail().isBlank()
                || !user.getEmail().contains("@")
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")
                || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Валидация не пройдена (User)");
            throw new ValidationException("Переданы ошибочные данные для User");
        }

        return true;
    }
}
