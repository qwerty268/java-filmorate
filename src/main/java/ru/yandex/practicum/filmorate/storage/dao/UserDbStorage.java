package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addUser(User user) {
        String sqlQuery = "INSERT INTO User" +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public Optional<User> getUserById(java.lang.Long id) {
        return Optional.empty();
    }

    private void updateFriendships(User user) {
        String sqlQuery = "SELECT * FROM Friend WHERE first_user_id = ? OR second_user_id = ?";

        Set<Friendship> friendships = new HashSet<>(jdbcTemplate.query(sqlQuery, this::friendshipFromSQL, user.getId()));
    }

    private Friendship friendshipFromSQL(ResultSet rs, Integer rowNum) throws SQLException {
        int user1_id = rs.getInt("first_user_id");

        return null;
    }
}
