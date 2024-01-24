package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.security.service.UserDetailsServiceImple;
import prueba14.sqldriver.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserDetailsServiceImple userDetailsServiceImple;

    ////----> CRUD

    //--> CREATE
    //SUSTITUIR POR REGISTER DE AUTHCONTROLLER??

    @PostMapping("/add")
    public ResponseEntity<Player> addPlayer(@RequestBody PlayerDto jugadorDTO){

        String userNameAutenticated = userDetailsServiceImple.getAuthenticatedUsername();

        if (!userNameAutenticated.equalsIgnoreCase(jugadorDTO.getUsername())){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(playerService.createPlayer(jugadorDTO), HttpStatus.CREATED);

    }

    @GetMapping("/get/findAll")
    public ResponseEntity<List<Player>> findAllPlayers(){

        return new ResponseEntity<>(playerService.findAllPlayers(), HttpStatus.FOUND);

    }

    @GetMapping("/get/getById/{id}")
    public ResponseEntity<Player> getOnePlayerByID(@PathVariable Long id){

        return ResponseEntity.ok(playerService.getOnePlayerByID(id));
    }

    @GetMapping("/get/getByUsername/{username}")
    public ResponseEntity<Player> getOnePlayerByUsername(@PathVariable String username){

        String userNameAutenticated = userDetailsServiceImple.getAuthenticatedUsername();

        if (!userNameAutenticated.equalsIgnoreCase(username)){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(playerService.getOnePlayerByUsername(username));
    }

    @PutMapping("/updatePlayer/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody PlayerDto playerDTO,
                                               @PathVariable Long id){

        String userNameAutenticated = userDetailsServiceImple.getAuthenticatedUsername();

        if (!userNameAutenticated.equalsIgnoreCase(playerDTO.getUsername())){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(playerService.updatePlayer(playerDTO, id), HttpStatus.OK);

    }


    @DeleteMapping("/delete/{id}")
    public void deletePlayer(@PathVariable Long id){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(id).getBody();

        if (!idAutenticado.equals(id)){

            throw new RuntimeException("No tienes permisos para borrar este jugador");
        }

        playerService.deletePlayer(id);
    }

    /////DADOS, FUNCIONES JUEGO

    @PostMapping("/dice/throw/{id}")
    public ResponseEntity<Dice> playerThrowDice(@PathVariable Long id){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(id).getBody();

        if (!idAutenticado.equals(id)){

            return new  ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(playerService.playerThrowDice(id));
    }
}
