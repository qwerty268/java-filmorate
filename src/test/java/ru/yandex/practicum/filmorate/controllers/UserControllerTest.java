package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController controller;
    private User user1;
    private User user2;

    @BeforeEach
    public void beforeEach() {
        controller = new UserController();

        user1 = User.builder()
                .birthday(LocalDate.of(2000, 10, 20))
                .email("lev-demchenko2003@yandex.ru")
                .id(1)
                .login("qwerty_268")
                .nickName("QWERTY_268")
                .build();

        user2 = User.builder()
                .birthday(LocalDate.of(2300, 10, 20))
                .email("lev-demchenko2003@yandex.ru")
                .id(2)
                .login("qwerty_268")
                .nickName("QWERTY_268")
                .build();
    }

    @Test
    public void addUserTest() {
        controller.addUser(user1);
        assertEquals(List.of(user1), controller.getUsers());

        ValidationException exception = assertThrows(ValidationException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User");


        user2 = user2.toBuilder()
                .birthday(LocalDate.of(2000, 10, 20))
                .email(" ")
                .build();

        exception = assertThrows(ValidationException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User");


        user2 = user2.toBuilder()
                .email("lev-demchenko2003-yandex.ru")
                .build();

        exception = assertThrows(ValidationException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User");


        user2 = user2.toBuilder()
                .email("lev-demchenko2003@yandex.ru")
                .login(" ")
                .build();

        exception = assertThrows(ValidationException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User");

        user2 = user2.toBuilder()
                .login("qwerty_268")
                .build();

        controller.addUser(user2);

        assertEquals(List.of(user1, user2), controller.getUsers());
    }

    @Test
    public void putUserTest() {
        controller.addUser(user1);
        controller.putUser(user1);
        assertEquals(List.of(user1), controller.getUsers());

        ValidationException exception = assertThrows(ValidationException.class, () ->  controller.putUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User");
    }

    @Test
    public void getUsersTest() {
        controller.addUser(user1);
        assertEquals(List.of(user1), controller.getUsers());
    }
}