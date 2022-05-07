package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage = new InMemoryUserStorage();

    public void addFriend(Long id, Long friendId) {
        User user1 = storage.getUserById(id);
        User user2 = storage.getUserById(friendId);

        user1.addFriend(user2);
        user2.addFriend(user1);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user1 = storage.getUserById(id);
        User user2 = storage.getUserById(friendId);

        user1.deleteFriend(user2);
        user2.deleteFriend(user1);
    }

    public List<User> getFriends(Long id) {
        Set<Long> idOfFriends = storage.getUserById(id).getFriends();
        List<User> friends = new ArrayList<>();

        idOfFriends.stream()
                .forEach((idOfFriend) -> friends.add(storage.getUserById(idOfFriend)));

        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> idOfFriends = storage.getUserById(id).getFriends();
        Set<Long> idOfOtherFriends = storage.getUserById(id).getFriends();

        List<User> friends = new ArrayList<>();

        idOfFriends.stream().forEach((idOfFriend) -> {
            if (idOfOtherFriends.contains(storage.getUserById(idOfFriend))) {
                friends.add(storage.getUserById(idOfFriend));
            }
        });


        return null;
    }
}
