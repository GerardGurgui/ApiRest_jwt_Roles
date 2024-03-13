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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        //MAPEAR DE DTO A ENTIDAD
        Player playerEntity = mapping.map(playerDto);

        //añadir rol USER
        Optional<Roles> roleUser = rolesRepository.findRoleByName("USER");

        roleUser.ifPresent(playerEntity::addRole);

        return playerRepository.save(playerEntity);

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


    /////FUNCIONALIDADES JUEGO (DADOS, PORCENTAJES)

    public Set<Dice> getThrows(Long id){

        Player player = getOnePlayerByID(id);

        if (player.getThrowsDices().isEmpty()){

            throw new PlayerNoDiceThrowsException(HttpStatus.NOT_FOUND, "This player don't have any dices throws");
        }

        return player.getThrowsDices();
    }

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

    public void deleteThrows(Long idPlayer){

        //buscar primero el jugador
        Player player = getOnePlayerByID(idPlayer);

        //comprobar que tiene tiradas
        if (player.getThrowsDices() != null && player.getThrowsDices().isEmpty()){

            throw new PlayerNoDiceThrowsException(HttpStatus.NOT_FOUND, "This player don't have any dices throws");
        }

        //buscar jugador a borrar tiradas

        Set<Dice> throwsDices = player.getThrowsDices();

        player.getThrowsDices().removeAll(throwsDices);

        playerRepository.save(player);

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
