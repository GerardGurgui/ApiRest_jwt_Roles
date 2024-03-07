package prueba14.sqldriver.servicesTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.repository.DiceRepository;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.repository.RolesRepository;
import prueba14.sqldriver.security.service.UserDetailsServiceImple;
import prueba14.sqldriver.service.AdminService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private DiceRepository diceRepository;
    @Mock
    private RolesRepository rolesRepository;
    @Mock
    private UserDetailsServiceImple userDetailsService;

    private Player playerTest;

    private Dice diceTest;

    private Set<Roles> roles;

    private Roles roleUser;

    private Roles roleAdmin;

    @BeforeAll
    public void setUp(){

        playerTest = new Player();
        playerTest.setId(1L);
        playerTest.setUsername("player1");
        playerTest.setPassword("password1");

        roles = new HashSet<>();

        roleUser = new Roles();
        roleUser.setId(1L);
        roleUser.setName("ROLE_USER");

        roleAdmin = new Roles();
        roleAdmin.setId(2L);
        roleAdmin.setName("ROLE_ADMIN");

        //añadimos solamente el rol de usuario al jugador
        //para poder probar el metodo modifyRole y añadir el rol de admin
        roles.add(roleUser);

        playerTest.setRoles(roles);

        diceTest = new Dice();

        diceTest.setId(1L);
        diceTest.setDado1(1);
        diceTest.setDado2(2);
        diceTest.setResultadoTirada(3);

        playerTest.addThrow(diceTest);

    }


    @Test
    public void deletePlayerTest(){

        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));

        adminService.deletePlayer(playerTest.getId());

        verify(playerRepository, times(1)).delete(playerTest);

        //comprobar que se ha eliminado el jugador
        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.empty());
        Optional<Player> playerOptional = playerRepository.findById(playerTest.getId());

        assertTrue(playerOptional.isEmpty());
    }

    @Test
    public void modifyRoleTest(){

        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));
        when(rolesRepository.findRoleByName(roleAdmin.getName())).thenReturn(Optional.of(roleAdmin));

        assertTrue(playerTest.getRoles().contains(roleUser));
        assertFalse(playerTest.getRoles().contains(roleAdmin));

        adminService.modifyRole(playerTest.getId(), roleAdmin.getName());

        assertTrue(playerTest.getRoles().contains(roleAdmin));

        verify(playerRepository, times(1)).save(playerTest);
    }

}
