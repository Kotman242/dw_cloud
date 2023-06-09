-- liquibase formatter sql

-- changeset andrewZ:1
CREATE TABLE IF NOT EXISTS users
(
    id       int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login    varchar(100) NOT NULL UNIQUE,
    password varchar NOT NULL
);
-- rollback DROP TABLE users;

-- changeset andrewZ:10
CREATE TABLE IF NOT EXISTS files
(
    id      int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    varchar NOT NULL,
    path    varchar NOT NULL UNIQUE,
    user_id int     NOT NULL
);
-- rollback DROP TABLE files;