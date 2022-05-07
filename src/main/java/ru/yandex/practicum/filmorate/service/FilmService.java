package ru.yandex.practicum.filmorate.service;

import org.apache.tomcat.jni.User;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

@Service
public class FilmService {
    private final FilmStorage storage = new InMemoryFilmStorage();
}
