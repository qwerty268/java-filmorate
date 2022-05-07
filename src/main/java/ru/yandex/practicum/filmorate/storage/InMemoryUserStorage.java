package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements  UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        if (filter(user)) {
            users.put(user.getId(), user);
        }
        log.debug("Добавлен новый Use: {}", user);
    }

    @Override
    public void updateUser(User user) {

        if (filter(user)) {
            users.put(user.getId(), user);
        }
        log.debug("User {} обновлен", user);
    }

    @Override
    public List<User> getUsers() {
        List<User> listOfUsers = new ArrayList<>(users.values());
        return listOfUsers;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }




    private boolean filter(User user) {
        StringBuilder builder = new StringBuilder();

        if (user.getEmail().isBlank()) {
            builder.append(" Email пустой;");
        } else if (!user.getEmail().contains("@")) {
            builder.append(" Email должен содержаь @;");
        }

        if (user.getLogin().isBlank()) {
            builder.append(" Логин не должен быть пустым;");
        } else if (user.getLogin().contains(" ")) {
            builder.append(" Логин не должен содержать пробелы;");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            builder.append(" Вы из будущего?");
        }

        String cause = builder.toString();
        if (!cause.isBlank()) {

            if (!cause.contains("?")) {
                cause = builder.replace(builder.length() - 1, builder.length(), ".").toString();
            }

            log.error("Валидация не пройдена (User):" + cause);
            throw new ValidationException("Переданы ошибочные данные для User:" + cause);
        }

        return true;
    }
}
