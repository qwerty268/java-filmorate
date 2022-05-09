package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
public abstract class Controller<T extends Film> {
    protected final Map<Long, T> storage = new HashMap<>();


    public void add(@RequestBody T t) {
        if (filter(t)) {
            storage.put(t.getId(), t);
        }
        log.debug("Добавлен новый {}: {}", t.getClass(),  t);
    }

    protected abstract boolean filter(T t);

}
