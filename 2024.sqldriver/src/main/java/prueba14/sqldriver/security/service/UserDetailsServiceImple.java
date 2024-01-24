package prueba14.sqldriver.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.repository.PlayerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import prueba14.sqldriver.security.user.CustomUserDetails;

import java.util.ArrayList;

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

    /*
     * Se añade la ruta de la clase User, ya que en este metodo no puede haber 2 clases iguales con spring
     * se interactura con nuestra bases de datos para usuarios, con el repositorio
     * */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Player player = playerRepository.getPlayerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        return new CustomUserDetails(
                player.getUsername(),
                player.getPassword(),
                new ArrayList<>(),
                player.getId()
        );
    }


    //METODO PARA VALIDAR QUE EL USUARIO AUTENTICADO SOLO PUEDE ACCEDER A SUS ACCIONES Y NO LAS DE TODOS LOS USERS
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
