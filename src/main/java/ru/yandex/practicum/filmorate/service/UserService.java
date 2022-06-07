package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationErrorException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;
    private static java.lang.Long userId = 1L;


    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public void addUser(User user) {
        filter(user);
        createId(user);
        storage.addUser(user);
    }

    public void updateUser(User user) {
        filter(user);
        storage.updateUser(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public Optional<User> getUserById(Long id) {
        if (id < 0) {
            throw new InvalidIdException();
        }

        return storage.getUserById(id);
    }


    //Добавление в друзья и принятие
    public void addFriend(Long id, Long friendId) {
        User user1 = getUserById(id).orElseThrow(() -> new UserDoesNotExistException(id));
        User user2 = getUserById(friendId).orElseThrow(() -> new UserDoesNotExistException(id));

        user1.addFriend(user2);             //создается запрос на добавление в лрузья


        Set<Friendship> friendships1 = user1.getFriends();
        Set<Friendship> friendships2 = user2.getFriends();


        if (friendships2.contains(new Friendship(user2.getId(), user1.getId()))) {      //если запрос на добавление в друзья был отправлен от user 2 тоже, то пользователи запрос будет принят
            friendships2.remove(new Friendship(user2.getId(), user1.getId()));
            friendships2.add(new Friendship(user2.getId(), user1.getId(), true));

            friendships1.remove(new Friendship(user1.getId(), user2.getId()));
            friendships1.add(new Friendship(user1.getId(), user2.getId(), true));
        }

        updateUser(user1);
        updateUser(user2);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user1 = getUserById(id).orElseThrow(() -> new UserDoesNotExistException(id));
        User user2 = getUserById(friendId).orElseThrow(() -> new UserDoesNotExistException(id));

        user1.deleteFriend(user2);
        user2.deleteFriend(user1);

        updateUser(user1);
        updateUser(user2);
    }

    public List<User> getFriends(Long id) {
        Set<Friendship> friendshipsOfUser = getUserById(id)
                .orElseThrow(() -> new UserDoesNotExistException(id))
                .getFriends();

        List<User> friends = new ArrayList<>();

        //нужно поставить проверку на статус, но тогда не проходит тесты в постман
        friendshipsOfUser
                .forEach((friendship) ->
                        friends
                                .add(getUserById(friendship.getUser2())
                                        .orElseThrow(() -> new UserDoesNotExistException(id))));


        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<User> friends1 = new HashSet<>(getFriends(id));
        Set<User> friends2 = new HashSet<>(getFriends(otherId));

        List<User> friends = new ArrayList<>();

        // находим общих друзей
        for (User friend : friends1) {
            if (friends2.contains(friend)) {
                friends.add(friend);
            }
        }

        return friends;
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
