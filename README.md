# README

## Application Description

This is a Spring Boot application that simulates a dice game. Players can throw dice and the results are stored in a database.

The scores of each player, the success rate, and the average of the dice results are recorded.

Players also have assigned roles, which can be 'user', 'admin' or 'superAdmin'. The 'superAdmin' is an in-memory user with all privileges.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Spring Security
- JWT
- Hibernate
- Maven

## Testing
- JUnit
- Mockito
- Integration Tests

## Security

The application uses Spring Security for authentication and authorization. Users are stored in a database and JWT is used for authentication.

Users with the 'admin' or 'superAdmin' role can perform all operations, while users with the 'user' role can only throw dice and view their own data.

## Registration System
The registration system is based on a token system, which are generated when logging in and are stored in the user's browser.

These tokens are used to authenticate the user in each request they make. The tokens have a duration of 24 hours, after which the user must log in again.

## Entities

The application has the following main entities:

- `Player`: Represents a player in the game. Each player has a set of roles and can throw dice.
- `Dice`: Represents a dice throw. Each dice throw is associated with a player.
- `Roles`: Represents the roles a player can have. The roles can be 'user', 'admin' or 'superAdmin'.

## Services

The application has the following main services:

- `AuthService`: Handles the authentication of users.
- `AdminService`: Handles the operations that can be performed by users with the 'admin' or 'superAdmin' role.
- `PlayerService`: The PlayerService in your application provides the main business logic for operations related to players and their dice throws. 

## Endpoints

### PlayerController

- `GET /players/get/findAll`: Gets all players.
- `GET /players/get/getById/{id}`: Gets a player by their ID.
- `GET /players/get/getByUsername/{username}`: Gets a player by their username.
- `PUT /players/updatePlayer/{id}`: Updates a player.
- `POST /players/dice/throw/{id}`: A player throws the dice.
- `GET /players/dice/get/{id}`: Gets all dice throws of a player.
- `DELETE /players/dice/deleteThrows/{id}`: Deletes all dice throws of a player.

### AdminController

- `POST /admin/{playerId}/{rolename}`: Modifies the role of a player. Only users with the 'admin' role can do this.
- `DELETE /admin/deleteUser/{idToDelete}`: Deletes a player. Only users with the 'admin' role can do this.

### AuthController

- `POST /api/auth/login`: Logs in a player and returns a JWT token.
- `POST /api/auth/register`: Registers a new player.

## Tests

The application includes tests for the services and controllers. The tests use JUnit and Mockito. The tests cover the following:

- `PlayerServiceTest`: Tests the methods in the `PlayerService`.
- `DiceServiceTest`: Tests the methods in the `DiceService`.
- `AuthServiceTest`: Tests the methods in the `AuthService`.
- `AdminServiceTest`: Tests the methods in the `AdminService`.
- `UserDetailsServiceImplTest`: Tests the `loadUserByUsername` method in the `UserDetailsServiceImpl`, including the case where the user is not found in the database and the username matches 'superAdmin'.
