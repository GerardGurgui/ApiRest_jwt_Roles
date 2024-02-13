--password = password1
INSERT INTO players (username, password, email) VALUES ('player1', '$2a$10$fssrkqbeGYAYM.Oh6mZKgelwAgkxBmDUN0iS64q.VJc0537qqpTOu', 'player1@player.com');
--password = password2
INSERT INTO players (username, password, email) VALUES ('player2', '$2a$10$zYRURT2K3k5KfbPUeermke1FF7k3qBocSHuuY0medJGxHJqMwjcxm', 'player2@player.com');
--password = password3
INSERT INTO players (username, password, email) VALUES ('player3', '$2a$10$m0qZkodUbvnEcBazxQAIcOVGMJ5h1W5RNAiLZ47DzgkhqhTpF4NgS', 'player3@player.com');

INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('ADMIN');

--/* PLAYER ADMIN */
-- Insertar el usuario administrador
INSERT INTO players (username, password, email) VALUES ('playerAdmin', '$2a$10$GNjFud9HO1JESWLI3DYppuGEDLZJ303vcalA.welPCe7yMKydMBxS', 'playeradmin@playeradmin.com');

-- Asignar el rol de administrador al usuario
INSERT INTO player_roles (id_player, id_role) VALUES ((SELECT id FROM players WHERE username = 'playerAdmin'), (SELECT id FROM roles WHERE name = 'ADMIN'));