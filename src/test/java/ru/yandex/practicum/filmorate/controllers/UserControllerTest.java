package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.yandex.practicum.filmorate.exceptions.ValidationErrorException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController controller;
    private User user1;
    private User user2;
    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void beforeEach() {
        controller = context.getBean(UserController.class);

        user1 = User.builder()
                .birthday(LocalDate.of(2000, 10, 20))
                .email("lev-demchenko2003@yandex.ru")
                .id(1)
                .login("qwerty_268")
                .name("QWERTY_268")
                .build();

        user2 = User.builder()
                .birthday(LocalDate.of(2300, 10, 20))
                .email("lev-demchenko2003@yandex.ru")
                .id(2)
                .login("qwerty_268")
                .name("QWERTY_268")
                .build();
    }

    @Test
    public void addUserTest() {
        controller.addUser(user1);
        assertEquals(List.of(user1), controller.getUsers());

        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User: Вы из будущего?");


        user2 = user2.toBuilder()
                .birthday(LocalDate.of(2000, 10, 20))
                .email(" ")
                .build();

        exception = assertThrows(ValidationErrorException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User: Email пустой.");


        user2 = user2.toBuilder()
                .email("lev-demchenko2003-yandex.ru")
                .build();

        exception = assertThrows(ValidationErrorException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User: Email должен содержаь @.");


        user2 = user2.toBuilder()
                .email("lev-demchenko2003@yandex.ru")
                .login(" ")
                .build();

        exception = assertThrows(ValidationErrorException.class, () ->  controller.addUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User: Логин не должен быть пустым.");

        user2 = user2.toBuilder()
                .login("qwerty_268")
                .build();

        controller.addUser(user2);

        assertEquals(List.of(user1, user2), controller.getUsers());
    }

    @Test
    public void putUserTest() {
        controller.addUser(user1);
        controller.updateUser(user1);
        assertEquals(List.of(user1), controller.getUsers());

        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () ->  controller.updateUser(user2));
        assertEquals(exception.getMessage(), "Переданы ошибочные данные для User: Вы из будущего?");
    }

    @Test
    public void getUsersTest() {
        controller.addUser(user1);
        assertEquals(List.of(user1), controller.getUsers());
    }
}