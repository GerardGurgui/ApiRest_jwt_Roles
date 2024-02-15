--password = password1
INSERT INTO players (username, password, email, puntuacion, winner, porcentaje_acierto, login_date)
VALUES ('player1', '$2a$10$fssrkqbeGYAYM.Oh6mZKgelwAgkxBmDUN0iS64q.VJc0537qqpTOu', 'player1@player.com', 0, 0, 0, CURRENT_DATE);

--password = password2
INSERT INTO players (username, password, email, puntuacion, winner, porcentaje_acierto, login_date)
VALUES ('player2', '$2a$10$zYRURT2K3k5KfbPUeermke1FF7k3qBocSHuuY0medJGxHJqMwjcxm', 'player2@player.com', 0, 0, 0, CURRENT_DATE);

--password = password3
INSERT INTO players (username, password, email, puntuacion, winner, porcentaje_acierto, login_date)
VALUES ('player3', '$2a$10$m0qZkodUbvnEcBazxQAIcOVGMJ5h1W5RNAiLZ47DzgkhqhTpF4NgS', 'player3@player.com', 0, 0, 0, CURRENT_DATE);

--/* REGISTRO TIRADAS */
INSERT INTO registro_tiradas (dado1, dado2, resultado_tirada, player_id) VALUES (1, 2, 3, (SELECT id FROM players WHERE username = 'player1'));
INSERT INTO registro_tiradas (dado1, dado2, resultado_tirada, player_id) VALUES (2, 3, 5, (SELECT id FROM players WHERE username = 'player2'));
INSERT INTO registro_tiradas (dado1, dado2, resultado_tirada, player_id) VALUES (3, 4, 7, (SELECT id FROM players WHERE username = 'player3'));
INSERT INTO registro_tiradas (dado1, dado2, resultado_tirada, player_id) VALUES (3, 4, 7, (SELECT id FROM players WHERE username = 'playerAdmin'));


--/* ROLES */
INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('ADMIN');

--/* PLAYER ADMIN */
-- Insertar el usuario administrador
INSERT INTO players (username, password, email) VALUES ('playerAdmin', '$2a$10$GNjFud9HO1JESWLI3DYppuGEDLZJ303vcalA.welPCe7yMKydMBxS', 'playeradmin@playeradmin.com');

-- Asignar el rol de administrador al usuario
INSERT INTO player_roles (id_player, id_role) VALUES ((SELECT id FROM players WHERE username = 'playerAdmin'), (SELECT id FROM roles WHERE name = 'ADMIN'));