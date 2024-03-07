package prueba14.sqldriver.servicesTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.security.service.UserDetailsServiceImple;
import prueba14.sqldriver.security.user.CustomUserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImple userDetailsServiceImpl;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private CustomUserDetails superAdmin;

    private Player playerUser;

    private Set<Roles> roles;

    private Roles roleUser;

    private Roles roleAdmin;

    @BeforeAll
    public void setUp(){

        playerUser = new Player();
        playerUser.setId(1L);
        playerUser.setUsername("player1");
        playerUser.setPassword("password1");

        roleUser = new Roles();
        roleUser.setId(1L);
        roleUser.setName("ROLE_USER");

        roleAdmin = new Roles();
        roleAdmin.setId(2L);
        roleAdmin.setName("ADMIN");

        when(superAdmin.getUsername()).thenReturn("superAdmin");
        when(superAdmin.getPassword()).thenReturn("password");
        when(superAdmin.getId()).thenReturn(1L);
    }

    @Test
    public void loadUserByUsernameTestUser(){

        roles = new HashSet<>();
        roles.add(roleUser);
        playerUser.setRoles(roles);

        when(playerRepository.getPlayerByUsername(playerUser.getUsername())).thenReturn(Optional.of(playerUser));

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsServiceImpl.loadUserByUsername(playerUser.getUsername());

        assert(customUserDetails.getUsername().equals(playerUser.getUsername()));
        assert(customUserDetails.getPassword().equals(playerUser.getPassword()));
        assert(customUserDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleUser.getName())));

        assertEquals(customUserDetails.getAuthorities().size(), 1);
        assertEquals(playerUser.getRoles().size(), 1);

    }

    @Test
    public void loadUserByUsernameTestAdmin(){

        roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);
        playerUser.setRoles(roles);

        when(playerRepository.getPlayerByUsername(playerUser.getUsername())).thenReturn(Optional.of(playerUser));

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsServiceImpl.loadUserByUsername(playerUser.getUsername());

        assert(customUserDetails.getUsername().equals(playerUser.getUsername()));
        assert(customUserDetails.getPassword().equals(playerUser.getPassword()));
        assert(customUserDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));

        /*
        * comprobar que el usuario tiene dos roles asignados, pero solamente uno de ellos es el que
        * otorga los permisos del rol en concreto (grantAuthority)
        */
        assertEquals(customUserDetails.getAuthorities().size(), 1);
        assertEquals(playerUser.getRoles().size(), 2);

    }


}
