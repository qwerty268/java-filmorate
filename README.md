# java-filmorate

![бд](https://dbdiagram.io/d/628ab4ddf040f104c174eec8)


POST /films - добавление фильма

PUT /films - обновление фильма

GET /films - возвращает все фильмы

GET /films/{id} - возвращает фильм по id

PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.

DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.

GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, вернёт первые 10.

POST /users - добавляет пользователя

PUT /users - обновляет пользователя

GET /users - возвращает всех пользователей

GET /users/{id} - возвращает пользователя по id

PUT /users/{id}/friends/{friendId} — добавление в друзья.

DELETE /users/{id}/friends/{friendId} — удаление из друзей.

GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.

GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
