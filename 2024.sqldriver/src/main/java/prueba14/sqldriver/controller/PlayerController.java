package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
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

        return ResponseEntity.ok(playerService.getOnePlayerByUsername(username));
    }

    @PutMapping("/updatePlayer/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody PlayerDto playerDTO,
                                               @PathVariable Long id){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(id).getBody();

        if (!idAutenticado.equals(id)){

            return new  ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(playerService.updatePlayer(playerDTO, id), HttpStatus.OK);

    }

    //solo el admin puede borrar jugadores por tanto decide que jugadores pueden ser borrados
    //--> falta implementar

    @DeleteMapping("/delete/{idAdmin}/{idToDelete}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePlayer(@PathVariable Long idAdmin, @PathVariable Long idToDelete){

        playerService.deletePlayer(idAdmin, idToDelete);
    }



    /////DADOS, FUNCIONES JUEGO, ROLES

    @PostMapping("/dice/throw/{id}")
    public ResponseEntity<Dice> playerThrowDice(@PathVariable Long id){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(id).getBody();

        if (!idAutenticado.equals(id)){

            return new  ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(playerService.playerThrowDice(id));
    }

    @DeleteMapping("/dice/delete/{idAdmin}/{idToDelete}")
    public ResponseEntity<String> deleteThrows(@PathVariable Long idAdmin, @PathVariable Long idToDelete){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(idAdmin).getBody();

        if (!idAutenticado.equals(idAdmin)){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        playerService.deleteThrows(idAdmin, idToDelete);
        return new ResponseEntity<>("Throws deleted for player " +idToDelete, HttpStatus.ACCEPTED);
    }

    ////ROLES
    @PostMapping("/roles/add/{playerId}/{rolename}")
    public void addRole(@PathVariable Long playerId, @PathVariable String rolename) {

        playerService.addRoleToPlayer(rolename, playerId);
    }

}
