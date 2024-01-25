# README

## Application Description

This is a Spring Boot application that simulates a dice game. Players can throw dice and the results are stored in a database.

The scores of each player, the success rate, and the average of the dice results are recorded.

Players also have assigned roles, which can be 'user' or 'admin'.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Spring Security
- JWT
- Hibernate
- Maven

## Security

The application uses Spring Security for authentication and authorization. Users are stored in a database and JWT is used for authentication.

Users with the 'admin' role can perform all operations, while users with the 'user' role can only throw dice and view their own data.

## Registration System
The registration system is based on a token system, which are generated when logging in and are stored in the user's browser.

These tokens are used to authenticate the user in each request they make. The tokens have a duration of 24 hours, after which the user must log in again.

## Entities

The application has the following main entities:

- `Player`: Represents a player in the game. Each player has a set of roles and can throw dice.
- `Dice`: Represents a dice throw. Each dice throw is associated with a player.
- `Roles`: Represents the roles a player can have. The roles can be 'user' or 'admin'.

## Endpoints

The application exposes the following endpoints:

- `POST /players/add`: Creates a new player.
- `GET /players/get/findAll`: Gets all players.
- `GET /players/get/getById/{id}`: Gets a player by their ID.
- `GET /players/get/getByUsername/{username}`: Gets a player by their username.
- `PUT /players/updatePlayer/{id}`: Updates a player.
- `DELETE /players/delete/{id}`: Deletes a player. Only users with the 'admin' role can do this.
- `POST /players/dice/throw/{id}`: A player throws the dice.
- `DELETE /players/dice/delete/{id}`: Deletes all dice throws of a player.
- `POST /players/roles/add/{playerId}/{rolename}`: Adds a role to a player.
