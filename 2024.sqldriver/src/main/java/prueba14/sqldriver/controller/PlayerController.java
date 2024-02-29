package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.security.service.UserDetailsServiceImple;
import prueba14.sqldriver.service.PlayerService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserDetailsServiceImple userDetailsServiceImple;


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
    public ResponseEntity<Player> updatePlayer(@Valid @RequestBody PlayerDto playerDTO,
                                               @PathVariable Long id){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(id).getBody();

        if (idAutenticado == null || !idAutenticado.equals(id)){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(playerService.updatePlayer(playerDTO, id), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{idToDelete}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePlayer(@PathVariable Long idToDelete){

        playerService.deletePlayer(idToDelete);
    }



    /////DADOS, FUNCIONES JUEGO, ROLES

    @PostMapping("/dice/throw/{id}")
    public ResponseEntity<Dice> playerThrowDice(@PathVariable Long id){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(id).getBody();

        if (idAutenticado == null || !idAutenticado.equals(id)){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(playerService.playerThrowDice(id));
    }

    @GetMapping("/dice/get/{id}")
    public ResponseEntity<Set<Dice>> getThrows(@PathVariable Long id){

        return ResponseEntity.ok(playerService.getThrows(id));
    }

    @DeleteMapping("/dice/delete/{idAdmin}/{idToDelete}")
    public ResponseEntity<String> deleteThrows(@PathVariable Long idAdmin, @PathVariable Long idToDelete){

        Long idAutenticado = userDetailsServiceImple.getAuthenticatedUserId(idAdmin).getBody();

        if (idAutenticado == null || !idAutenticado.equals(idAdmin)){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        playerService.deleteThrows(idAdmin, idToDelete);

        return new ResponseEntity<>("Throws deleted for player " +idToDelete, HttpStatus.ACCEPTED);
    }

    ////ROLES
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles/add/{playerId}/{rolename}")
    public void addRole(@PathVariable Long playerId, @PathVariable String rolename) {

        playerService.addRoleToPlayer(rolename, playerId);
    }

}
