package prueba14.sqldriver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.exceptions.PlayerNotFoundException;
import prueba14.sqldriver.exceptions.RolNotFoundException;
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
    private UserDetailsServiceImple userDetailsService;


    public void modifyRole(Long playerId, String rolename) {

        //comprobar si existe el jugador
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(HttpStatus.NOT_FOUND, "Player not found"));

        //comprobar si existe el rol
        Roles role = rolesRepository.findRoleByName(rolename)
                .orElseThrow(() -> new RolNotFoundException(HttpStatus.NOT_FOUND, "Role not found"));

        Set<Roles> roles = player.getRoles();
        roles.add(role);
        player.setRoles(roles);
        playerRepository.save(player);

        //actualizar el rol del usuario customUserDetails con grantAuthority
        userDetailsService.loadUserByUsername(player.getUsername());

    }


}
