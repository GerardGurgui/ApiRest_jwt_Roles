package prueba14.sqldriver.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.repository.PlayerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import prueba14.sqldriver.security.user.CustomUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Autentica un usuario de la base de datos
 *
 * Authentication Manager llama al método loadUserByUsername de esta clase
 * para obtener los detalles del usuario de la base de datos cuando
 * se intente autenticar un usuario
 */

@Service
public class UserDetailsServiceImple implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CustomUserDetails superAdmin;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Player> playerOpt = playerRepository.getPlayerByUsername(username);

        // Comprueba si el usuario existe en la base de datos
        if (playerOpt.isPresent() && playerOpt.get().getUsername().equals(username)) {

            Player player = playerOpt.get();

            // asigna los roles del usuario
            Collection<? extends GrantedAuthority> authorities = getAuthorities(player);

            return new CustomUserDetails(
                    player.getUsername(),
                    player.getPassword(),
                    authorities,
                    player.getId()
            );

            // si el usuario no existe en la base de datos, comprueba si es el usuario en memoria
        } else if (playerOpt.isEmpty() && superAdmin.getUsername().equals(username)) {

            return superAdmin;

        } else {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
    }


    //asigna roles dependiendo de si el usuario es admin o no
    private Collection<? extends GrantedAuthority> getAuthorities(Player player) {

        if (player.isAdmin()){
            return getAdminAuthorities();
        } else {
            return getUserAuthorities();
        }
    }



    private Collection<? extends GrantedAuthority> getUserAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        authorities.add(new SimpleGrantedAuthority("READ"));
        return authorities;
    }

    private Collection<? extends GrantedAuthority> getAdminAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }


    public String getAuthenticatedUsername() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public ResponseEntity<Long> getAuthenticatedUserId(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getId() != id){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userDetails.getId(), HttpStatus.OK);
    }

}
