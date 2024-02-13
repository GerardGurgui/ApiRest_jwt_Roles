CREATE TABLE players (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    puntuacion INT,
    winner INT,
    porcentaje_acierto INT,
    login_date DATE
);

CREATE TABLE registro_tiradas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dado1 INT,
    dado2 INT,
    resultado_tirada INT,
    player_id BIGINT,
    id_player BIGINT,
    FOREIGN KEY (player_id) REFERENCES players(id)
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);


CREATE TABLE player_roles (
    id_player BIGINT,
    id_role BIGINT,
    PRIMARY KEY (id_player, id_role),
    FOREIGN KEY (id_player) REFERENCES players(id),
    FOREIGN KEY (id_role) REFERENCES roles(id)
);