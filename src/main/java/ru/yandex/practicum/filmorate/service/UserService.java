package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationErrorException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
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
        Set<Long> idOfFriends = getUserById(id).getFriends();
        List<User> friends = new ArrayList<>();

        idOfFriends.stream()
                .forEach((idOfFriend) -> friends.add(getUserById(idOfFriend)));

        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> friendsId1 = getUserById(id).getFriends();
        Set<Long> friendsId2 = getUserById(otherId).getFriends();

        List<User> friends = new ArrayList<>();

        // находим общих друзей
        for (Long idOfFriend: friendsId1) {
            if (friendsId2.contains(idOfFriend)) {
                friends.add(getUserById(idOfFriend));
            }
        }

        return friends;
    }
}
