package prueba14.sqldriver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(Player.class);
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private DiceRepository diceRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private Map mapping;


    /////CRUD

    ////////CREATE
    public Player createPlayer(PlayerDto playerDto){

        //MAPEAR DE DTO A ENTIDAD (COMPROBAR NOMBRE VACÍO)
        Player playerEntity = mapping.map(playerDto);

        boolean playerExists = playerRepository.existsByUsername(playerDto.getUsername());

        if (playerExists && !playerEntity.getUsername().equalsIgnoreCase("Anonimo")){

            throw new ExistentUsernameException(HttpStatus.FOUND,"this username is already taken");
        }

        log.info("jugador creado correctamente");

        return playerRepository.save(playerEntity);

    }

    public void addRoleToPlayer(String rolename, Long playerId){

        //comprobar si existe el jugador
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(HttpStatus.NOT_FOUND, "Player not found"));

        //comprobar si existe el rol
        Roles role = rolesRepository.findByName(rolename)
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

            throw new PlayerNotFoundException(HttpStatus.NOT_FOUND,"El jugador con username " + username + " no existe ");
        }

        if (username.equalsIgnoreCase("Anonimo") && findAllPlayers().size() <= 2){

            throw new PlayerNotFoundException(HttpStatus.CONFLICT, "Existe más de un usuario con el nombre anonimo");
        }

        return playerRepository.getPlayerByUsername(username).get();
    }

    /////////UPDATE

    public Player updatePlayer(PlayerDto playerDto, Long id){

        Player playerToUpdate = getOnePlayerByID(id);

        if (playerRepository.existsByEmail(playerDto.getEmail())){

            throw new ExistentEmailException(HttpStatus.FOUND,"El email " +playerDto.getEmail()+ " ya existe");
        }

        if (playerRepository.existsByUsername(playerDto.getUsername())){

            throw new ExistentUsernameException(HttpStatus.FOUND, "El usuario con nombre " +playerDto.getUsername() + " ya existe ");
        }

        playerToUpdate.setUsername(playerDto.getUsername());
        playerToUpdate.setEmail(playerDto.getEmail());

        return playerRepository.save(playerToUpdate);
    }

    ////////DELETE

    public void deletePlayer(Long id){

        Player playerToDelete = getOnePlayerByID(id);

        playerRepository.delete(playerToDelete);

    }

    /////FUNCIONALIDADES JUEGOS (TIRADAS DE DADOS, PORCENTAJES ETC)

    public Dice playerThrowDice(Long id){

        Player player = getOnePlayerByID(id);

        //TIRADA DE DADOS Y LA SUMA
        Dice dice = GameUtilities.LaunchDice();

        //COMPROBAR PUNTUACION
        checkPuntuationAndAverage(player,dice);

        //AÑADIR TIRADA AL JUGADOR
        asignarTirada(player,dice);

        return diceRepository.save(dice);
    }

    //COMPRUEBA PUNTUACION DE RONDA Y PUNTUACION DE PARTIDA, INSERTA PORCENTAJES
    public void checkPuntuationAndAverage(Player player, Dice dice){


        boolean rondaGanada = GameUtilities.checkWinRound(dice.getResultadoTirada());

        if (rondaGanada){

            sumarPuntuacion(player);
            boolean ganadorPartida = GameUtilities.checkWinGame(player.getPuntuacion());

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

        int porcentaje = GameUtilities.checkAveragePlayer(player);
        player.setAcierto(porcentaje);
    }
}
