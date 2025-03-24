CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);

INSERT INTO users (password, username, role)
VALUES ('$2a$10$0xKaKGcMAn3q/eIbn2t9LO7zfUgkbzFM.1p1euB9SCVX9BmnBdpWe', 'admin', 'ADMIN'),
       ('$2a$10$poKwgC2MH1vOy8PWEddyKuJWndxLcKvmBIPMf6kn2UwEE6jFoON8a', 'user', 'USER');