package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserStorage storage = new InMemoryUserStorage();

    @PostMapping("/users")
    public void addUser(@RequestBody User user) {
        storage.addUser(user);
    }

    @PutMapping("/users")
    public void updateUser(@RequestBody User user) {
        storage.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return storage.getUsers();
    }
}
