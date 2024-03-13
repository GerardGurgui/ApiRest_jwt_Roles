package prueba14.sqldriver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.exceptions.PlayerNotFoundException;
import prueba14.sqldriver.exceptions.RolNotFoundException;
import prueba14.sqldriver.repository.DiceRepository;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.repository.RolesRepository;
import prueba14.sqldriver.security.service.UserDetailsServiceImple;

import java.util.*;

@Service
public class AdminService {

    @Autowired
    private  PlayerRepository playerRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private DiceRepository diceRepository;
    @Autowired
    private UserDetailsServiceImple userDetailsService;

    private Player getPlayer(Long playerId){

        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    private Roles getRole(String rolename){

        return rolesRepository.findRoleByName(rolename)
                .orElseThrow(() -> new RolNotFoundException(HttpStatus.NOT_FOUND, "Role not found"));
    }


    public void addRole(Long playerId, String rolename) {

        //comprobar si existe el jugador
        Player player = getPlayer(playerId);

        //comprobar si existe el rol
        Roles role = getRole(rolename);

        Set<Roles> roles = player.getRoles();
        roles.add(role);
        player.setRoles(roles);
        playerRepository.save(player);

        //actualizar el rol del usuario customUserDetails con grantAuthority
        userDetailsService.loadUserByUsername(player.getUsername());

    }

    public void deleteRole(Long playerId,String rolename){

        //comprobar si existe el jugador
        Player player = getPlayer(playerId);

        //comprobar si existe el rol
        Roles role = getRole(rolename);

        if (role.getName().equalsIgnoreCase("USER")){
            throw new IllegalArgumentException("You can't delete the USER role");
        }

        Set<Roles> roles = player.getRoles();
        roles.remove(role);
        player.setRoles(roles);
        playerRepository.save(player);

        //actualizar el rol del usuario customUserDetails con grantAuthority
        userDetailsService.loadUserByUsername(player.getUsername());
    }


    public void deleteUser(Long idToDelete){

        Player playerToDelete = getPlayer(idToDelete);

        if (!playerToDelete.getRoles().isEmpty()){

            playerToDelete.getRoles().clear();
        }

        if (!playerToDelete.getThrowsDices().isEmpty()){

            Set<Dice> throwsDices = playerToDelete.getThrowsDices();

            playerToDelete.getThrowsDices().removeAll(throwsDices);

            diceRepository.deleteAll(throwsDices);
        }

        playerRepository.delete(playerToDelete);
    }

}
