package prueba14.sqldriver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.mapper.Map;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.repository.RolesRepository;
import prueba14.sqldriver.security.user.CustomUserDetails;

import java.util.*;

@Service
public class AdminService {

    @Autowired
    private  PlayerRepository playerRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Map mapping;


    //ATRIBUTO ADMIN DE CADA USUARIO SOLAMENTE REPRESNTATIVO, NO TIENE IMPACTO EN LA AUTORIZACION REAL
    public Player createAdmin(PlayerDto playerDto){

        Player admin = mapping.map(playerDto);

        Set<Roles> roles = new HashSet<>();
        roles.add(rolesRepository.findRoleByName("ADMIN").get());

        admin.setRoles(roles);

        return playerRepository.save(admin);
    }


}
