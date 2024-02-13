package prueba14.sqldriver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.exceptions.*;
import prueba14.sqldriver.gameUtilities.GameUtilities;
import prueba14.sqldriver.mapper.Map;
import prueba14.sqldriver.repository.DiceRepository;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.repository.RolesRepository;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private DiceRepository diceRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private GameUtilities gameUtilities;
    @Autowired
    private Map mapping;


    /////CRUD

    ////////CREATE
    public Player createPlayer(PlayerDto playerDto){

        //MAPEAR DE DTO A ENTIDAD (COMPROBAR NOMBRE VACÍO)
        //COMPROBAR SI EXISTE EL USERNAME O EMAIL EN AUTHCONTROLLER REGISTER
        Player playerEntity = mapping.map(playerDto);

        return playerRepository.save(playerEntity);

    }

    public void addRoleToPlayer(String rolename, Long playerId){

        //comprobar si existe el jugador
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(HttpStatus.NOT_FOUND, "Player not found"));

        //comprobar si existe el rol
        Roles role = rolesRepository.findRoleByName(rolename)
                .orElseThrow(() -> new RolNotFoundException(HttpStatus.NOT_FOUND, "Role not found"));

        //comprobar si el admin ya está asignado a un usuario (solo puede haber un admin)
        if (rolename.equalsIgnoreCase("admin") && rolesRepository.existsByRoleName("admin")) {

            throw new AdminAlreadyExistsException(HttpStatus.CONFLICT, "admin already exists");
        }

        player.addRole(role);
        playerRepository.save(player);
    }

    ////////READ

    public List<Player> findAllPlayers(){

        return playerRepository.findAll();

    }

    public Player getOnePlayerByID(Long id){

        boolean existsById = playerRepository.existsById(id);

        if (!existsById){

            throw new PlayerNotFoundException(HttpStatus.NOT_FOUND,"player with id " + id + " doesn`t exist ");
        }

        return playerRepository.findById(id).get();
    }

    public Player getOnePlayerByUsername(String username){

        boolean existsUsername = playerRepository.existsByUsername(username);

        if (!existsUsername){

            throw new PlayerNotFoundException(HttpStatus.NOT_FOUND, "player " + username + "doesn`t exist");
        }

        return playerRepository.getPlayerByUsername(username).get();
    }

    /////////UPDATE

    public Player updatePlayer(PlayerDto playerDto, Long id){

        Player playerToUpdate = getOnePlayerByID(id);

        if (playerRepository.existsByEmail(playerDto.getEmail())){

            throw new ExistentEmailException(HttpStatus.FOUND,"Email" +playerDto.getEmail()+ " already exists");
        }

        if (playerRepository.existsByUsername(playerDto.getUsername())){

            throw new ExistentUsernameException(HttpStatus.FOUND, "User " +playerDto.getUsername() + " already exists");
        }

        playerToUpdate = mapping.mapUpdate(playerDto, playerToUpdate);

        return playerRepository.save(playerToUpdate);
    }

    ////////DELETE

    public void deletePlayer(Long idPlayer, Long idToDelete){

        //buscar primero el jugador
        Player playerIsAdmin = getOnePlayerByID(idPlayer);

        //comprobar si es admin
        boolean isAdmin = playerIsAdmin.isAdmin();

        if (!isAdmin){

            throw new UserUnauthorizedException(HttpStatus.UNAUTHORIZED, "this user is not an admin");
        }

        Player playerToDelete = getOnePlayerByID(idToDelete);

        playerRepository.delete(playerToDelete);
    }

    /////FUNCIONALIDADES JUEGO (DADOS, PORCENTAJES)

    public Dice playerThrowDice(Long id){

        Player player = getOnePlayerByID(id);

        //TIRADA DE DADOS Y LA SUMA
        Dice dice = gameUtilities.LaunchDice();

        //COMPROBAR PUNTUACION
        checkPuntuationAndAverage(player,dice);

        //AÑADIR TIRADA AL JUGADOR
        asignarTirada(player,dice);

        return diceRepository.save(dice);
    }

    public void deleteThrows(Long idPlayer,Long idToDeleteThrows){

        //buscar primero el jugador
        Player player = getOnePlayerByID(idPlayer);

        //comprobar si es admin
        boolean isAdmin = player.isAdmin();

        if (!isAdmin){

            throw new RolNotFoundException(HttpStatus.NOT_FOUND, "this user is not an admin");
        }

        //buscar jugador a borrar tiradas
        Player playerToDeleteThrows = getOnePlayerByID(idToDeleteThrows);

        if (playerToDeleteThrows.getThrowsDices().isEmpty()){

            throw new PlayerNoDiceThrowsException(HttpStatus.NOT_FOUND,"This player don't have any dices throws");
        }

        playerToDeleteThrows.getThrowsDices().clear();

        playerRepository.save(playerToDeleteThrows);

    }

    //COMPRUEBA PUNTUACION DE RONDA Y PUNTUACION DE PARTIDA, INSERTA PORCENTAJES
    public void checkPuntuationAndAverage(Player player, Dice dice){


        boolean rondaGanada = gameUtilities.checkWinRound(dice.getResultadoTirada());

        if (rondaGanada){

            sumarPuntuacion(player);
            boolean ganadorPartida = gameUtilities.checkWinGame(player.getPuntuacion());

            //COMPROBAR GANADOR DE LA PARTIDA
            if (ganadorPartida){

                player.setWinner(1);
            }
        }else {

            player.setAcierto(0);
        }

    }
    public void sumarPuntuacion(Player player){

        int puntuacion = player.getPuntuacion();
        puntuacion++;
        player.setPuntuacion(puntuacion);
        player.setAcierto(100);
    }

    public void asignarTirada(Player player, Dice dice){

        player.addThrow(dice);

        int porcentaje = gameUtilities.checkAveragePlayer(player);
        player.setAcierto(porcentaje);
    }
}
