CREATE TABLE IF NOT EXISTS Genre
(
    genre_id INTEGER generated by default as identity primary key,
    genre    VARCHAR
);

CREATE TABLE IF NOT EXISTS mpaa_rating
(
    mpaa_rating_id INTEGER PRIMARY KEY,
    rating_value   VARCHAR
);

CREATE TABLE IF NOT EXISTS Film
(
    id           IDENTITY GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR NOT NULL,
    description  TEXT,
    release_date DATE,
    duration     INTERVAL SECOND,
    mpaa_rating  INTEGER REFERENCES mpaa_rating (mpaa_rating_id)
);

CREATE TABLE IF NOT EXISTS Film_genre
(
    film_id  INTEGER PRIMARY KEY REFERENCES Film (id),
    genre_id VARCHAR REFERENCES Genre (genre_id)
);

CREATE TABLE IF NOT EXISTS User
(
    id           INTEGER IDENTITY GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email        VARCHAR NOT NULL,
    login        VARCHAR NOT NULL,
    name         VARCHAR NOT NULL,
    birthday     DATE,
    country_code INTEGER
);

CREATE TABLE IF NOT EXISTS Friend
(
    first_user_id  INTEGER PRIMARY KEY REFERENCES User (id),
    second_user_id INTEGER REFERENCES User (id),
    status         BOOLEAN
);

CREATE TABLE IF NOT EXISTS Likes
(
    film_id INTEGER REFERENCES Film (id),
    user_id INTEGER REFERENCES User (id)
)