package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
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

    public User getUserById(Long id) {
        return storage.getUserById(id).orElseThrow(() -> new UserDoesNotExistException(id));
    }

    public void addFriend(Long id, Long friendId) {
        User user1 = getUserById(id);
        User user2 = getUserById(friendId);

        user1.addFriend(user2);
        user2.addFriend(user1);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user1 = getUserById(id);
        User user2 = getUserById(friendId);

        user1.deleteFriend(user2);
        user2.deleteFriend(user1);
    }

    public List<User> getFriends(Long id) {
        Set<Friendship> friendshipsOfUser = getUserById(id).getFriends();
        List<User> friends = new ArrayList<>();

        friendshipsOfUser.stream()
                .forEach((friendship) -> {
                    friends.add(getUserById(friendship.getUser2()));
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
