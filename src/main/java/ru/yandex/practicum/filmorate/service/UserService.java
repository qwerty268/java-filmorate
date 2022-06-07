package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public void addUser(User user) {
        storage.addUser(user);
    }

    public void updateUser(User user) {
        storage.updateUser(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public Optional<User> getUserById(Long id) {
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

        friendshipsOfUser
                .forEach((friendship) -> {
                    if (friendship.getStatus() == true) {
                        friends
                                .add(getUserById(friendship.getUser2())
                                        .orElseThrow(() -> new UserDoesNotExistException(id)));
                    }
                });

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
}
