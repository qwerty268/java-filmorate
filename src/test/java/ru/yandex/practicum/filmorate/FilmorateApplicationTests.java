package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserService userService;
    private final FilmService filmService;

    private final JdbcTemplate jdbcTemplate;

    private User user1;
    private User user2;

    private Film film1;
    private Film film2;

    @BeforeEach
    public void createDate() {

        String sqlQuery = "DELETE FROM Friend;" +
                "DELETE FROM Likes;" +
                "DELETE FROM Film_genre;" +
                "DELETE FROM Film;" +
                "DELETE FROM User;";
        jdbcTemplate.update(sqlQuery);

        user1 = new User(1L,
                "test@test.ru",
                "login",
                "name",
                LocalDate.of(2010, 1, 3),
                new HashSet<>());
        userService.addUser(user1);

        user2 = new User(2L,
                "test@test.ru",
                "login",
                "name",
                LocalDate.of(2010, 1, 3),
                new HashSet<>());
        userService.addUser(user2);

        film1 = new Film(1,
                "name1",
                "description1",
                LocalDate.of(2020, 2, 2),
                Duration.ZERO,
                new HashSet<>(),
                List.of(Genre.CARTOON, Genre.ACTION),
                MPA.G);
        filmService.addFilm(film1);

        film2 = new Film(2,
                "name2",
                "description2",
                LocalDate.of(2000, 2, 2),
                Duration.ofHours(1),
                new HashSet<>(),
                List.of(Genre.DOCUMENTARY, Genre.ACTION),
                MPA.PG);
        filmService.addFilm(film2);
    }


    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userService.getUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetUsers() {
        List<User> users = userService.getUsers();

        assertEquals(users, List.of(user1, user2));
    }

    @Test
    public void testUpdateUser() {
        user2 = new User(2L,
                "updatet@test.ru",
                "updatedLogin",
                "updatedName",
                LocalDate.of(2003, 1, 3),
                new HashSet<>());

        userService.updateUser(user2);
        User userFromStorage = userService.getUserById(2L).get();
        assertTrue(user2.everyFieldEquals(userFromStorage));
    }

    @Test
    public void testAddingGettingDeletingFriend() {
        userService.addFriend(1L, 2L);
        assertTrue(userService.getFriends(1L).isEmpty());

        userService.addFriend(2L, 1L); //приняте заявки в друзья
        Set<User> friendshipSet = new HashSet<>(userService.getFriends(2L));
        assertTrue(friendshipSet.contains(user1));

        userService.deleteFriend(1L, 2L);
        assertTrue(userService.getFriends(1L).isEmpty());

    }

    @Test
    public void testGetFilms() {
        assertTrue(filmService.getFilms().equals(List.of(film1, film2)));
    }

    @Test
    public void updateFilm() {
        film1 = new Film(1,
                "name2",
                "description2",
                LocalDate.of(2023, 2, 2),
                Duration.ZERO,
                new HashSet<>(),
                List.of(Genre.CARTOON, Genre.ACTION),
                MPA.G);

        filmService.updateFilm(film1);

        assertEquals(film1, filmService.getFilmById(1L));
    }

    @Test
    public void putLike() {
        filmService.putLike(1L, 1L);

        assertEquals(filmService.getFilmById(1L).getLikes(), new HashSet<>(List.of(1L)));
    }

    @Test
    public void removeLike() {
        filmService.putLike(1L, 1L);
        filmService.removeLike(1L, 1L);

        assertEquals(filmService.getFilmById(1L).getLikes(), new HashSet<>());
    }
}