# README

## Descripción de la aplicación

Aplicación de Spring Boot que simula un juego de dados. Los jugadores pueden lanzar dados y los resultados se almacenan en una base de datos.

Se registran las puntuaciones de cada jugador, el porcentaje de acierto y la media de los resultados de los dados.

Los jugadores también tienen roles asignados, que pueden ser 'user' o 'admin'.

## Tecnologías utilizadas

- Java
- Spring Boot
- Spting Data JPA
- MySQL
- Spring Security
- JWT
- Hibernate
- Maven
## Seguridad

La aplicación utiliza Spring Security para la autenticación y la autorización. Los usuarios se almacenan en una base de datos y se utiliza JWT para la autenticación.

Los usuarios con el rol 'admin' pueden realizar todas las operaciones, mientras que los usuarios con el rol 'user' solo pueden lanzar dados y ver sus propios datos.

## Sistema de registro
el sistema de registro se basa en un sistema de tokens, que se generan al iniciar sesión y se almacenan en el navegador del usuario. 

Estos tokens se utilizan para autenticar al usuario en cada solicitud que realice. Los tokens tienen una duración de 24 horas, tras las cuales el usuario debe volver a iniciar sesión.

## Entidades

La aplicación tiene las siguientes entidades principales:

- `Player`: Representa a un jugador en el juego. Cada jugador tiene un conjunto de roles y puede lanzar dados.
- `Dice`: Representa un lanzamiento de dados. Cada lanzamiento de dados está asociado con un jugador.
- `Roles`: Representa los roles que puede tener un jugador. Los roles pueden ser 'user' o 'admin'.

## Endpoints

La aplicación expone los siguientes endpoints:

- `POST /players/add`: Crea un nuevo jugador.
- `GET /players/get/findAll`: Obtiene todos los jugadores.
- `GET /players/get/getById/{id}`: Obtiene un jugador por su ID.
- `GET /players/get/getByUsername/{username}`: Obtiene un jugador por su nombre de usuario.
- `PUT /players/updatePlayer/{id}`: Actualiza un jugador.
- `DELETE /players/delete/{id}`: Elimina un jugador. Solo los usuarios con el rol 'admin' pueden hacer esto.
- `POST /players/dice/throw/{id}`: Un jugador lanza los dados.
- `DELETE /players/dice/delete/{id}`: Elimina todos los lanzamientos de dados de un jugador.
- `POST /players/roles/add/{playerId}/{rolename}`: Añade un rol a un jugador.


