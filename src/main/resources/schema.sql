create table if not exists genres
(
    genre_id integer primary key,
    genre_name varchar(100)
);

create table if not exists rating_mpa
(
    rating_id integer primary key,
    rating_name varchar(100)
);