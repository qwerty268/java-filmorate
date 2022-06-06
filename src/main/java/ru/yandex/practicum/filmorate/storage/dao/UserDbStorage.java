package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addUser(User user) {
        String sqlQuery = "INSERT INTO USER" +
                " VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        updateFriendships(user);
    }

    @Override
    public void updateUser(User user) {
        String sqlQuery = "UPDATE USER SET " +
                "id = ?," +
                "email = ?," +
                "login = ?," +
                "name = ?," +
                "birthday = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        updateFriendships(user);
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM USER";


        return jdbcTemplate.query(sqlQuery, this::userFromSQL);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        String sqlQuery = "SELECT * FROM USER WHERE id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::userFromSQL, id));
    }

    private void updateFriendships(User user) {
        String sqlQuery = "DELETE FROM Friend WHERE  first_user_id = ? OR (second_user_id = ? AND status = TRUE)";

        jdbcTemplate.update(sqlQuery, user.getId(), user.getId());

        sqlQuery = "INSERT INTO Friend VALUES (?, ?, ?)";

        if (user.getFriends() != null) {
            for (Friendship friendship : user.getFriends()) {
                jdbcTemplate.update(sqlQuery, friendship.getUser1(), friendship.getUser2(), friendship.getStatus());
            }
        }
    }

    private User userFromSQL(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = LocalDate.ofInstant(rs.getDate("date").toInstant(), ZoneId.systemDefault());

        String sqlQuery = "SELECT * FROM Friend WHERE first_user_id = ? OR second_user_id = ?";

        Set<Friendship> friendshipSet = new HashSet<>(jdbcTemplate.query(sqlQuery, this::friendshipFromSQL, id, id));

        return new User(id, email, login, name, birthday, friendshipSet);
    }

    private Friendship friendshipFromSQL(ResultSet rs, Integer rowNum) throws SQLException {
        Long user1_id = rs.getLong("first_user_id");
        Long user2_id = rs.getLong("second_user_id");
        boolean status = rs.getBoolean("status");

        return new Friendship(user1_id, user2_id, status);
    }
}
