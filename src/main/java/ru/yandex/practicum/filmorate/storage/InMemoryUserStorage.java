package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationErrorException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private static java.lang.Long userId = 1L;

    @Override
    public void addUser(User user) {
        if (filter(user)) {
            createId(user);
            users.put(user.getId(), user);
            log.debug("Добавлен новый User: {}", user);
        }
    }

    @Override
    public void updateUser(User user) {

        if (filter(user) && users.get(user.getId()) != null) {

            users.put(user.getId(), user);
            log.debug("User {} обновлен", user);
        }
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
            throw new ValidationErrorException("Переданы ошибочные данные для User:" + cause);
        }

        return true;
    }

    private void createId(User user) {
        if (user.getId() == 0) {
            user.setId(userId);
            userId++;
        }
    }
}
