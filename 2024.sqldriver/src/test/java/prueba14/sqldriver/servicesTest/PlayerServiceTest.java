package prueba14.sqldriver.servicesTest;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.exceptions.AdminAlreadyExistsException;
import prueba14.sqldriver.exceptions.RolNotFoundException;
import prueba14.sqldriver.gameUtilities.GameUtilities;
import prueba14.sqldriver.mapper.Map;
import prueba14.sqldriver.repository.DiceRepository;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.repository.RolesRepository;
import prueba14.sqldriver.service.PlayerService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application-test.properties")
public class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private DiceRepository diceRepository;
    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private Map mapping;

    @Mock
    private GameUtilities gameUtilities;

    private Dice diceTest = new Dice();

    private Player playerTest = new Player();

    private PlayerDto playerDtoTest = new PlayerDto();

    private Player playerToDelete = new Player();

    private Player playerNotAdmin = new Player();

    private Set<Roles> roles;

    private Roles roleUser = new Roles();

    private Roles roleAdmin = new Roles();



    @BeforeAll
    public void setUp(){

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        mapping = new Map(encoder);

        playerTest.setId(1L);
        playerTest.setUsername("playerTest");
        playerTest.setPassword("playerTest");
        playerTest.setEmail("playerTest");
        playerTest.setRoles(new HashSet<>());

        //DADOS
        diceTest.setDado1(1);
        diceTest.setDado2(1);
        diceTest.setResultadoTirada(2);

        //DTO
        playerDtoTest.setUsername("playerDtoTest");
        playerDtoTest.setPassword("playerDtoTest");
        playerDtoTest.setEmail("playerDtoTest");

        //ROLES
        roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);

        roleUser.setId(1L);
        roleUser.setName("USER");

        roleAdmin.setId(2L);
        roleAdmin.setName("ADMIN");

        ////DELETE
        playerToDelete.setId(2L);

        playerNotAdmin.setId(3L);
        playerNotAdmin.setRoles(new HashSet<>());
        playerNotAdmin.addRole(roleUser);

    }

    ////CREATE

    @Test
    public void createPlayerTest(){

        when(mapping.map(playerDtoTest)).thenReturn(playerTest);
        when(playerRepository.save(any(Player.class))).thenReturn(playerTest);

        Player playerResult = playerService.createPlayer(playerDtoTest);

        assertEquals(playerResult.getUsername(), playerTest.getUsername());
        assertEquals(playerResult.getEmail(), playerTest.getEmail());
        assertEquals(playerResult.getPassword(), playerTest.getPassword());

        verify(playerRepository, times(1)).save(playerTest);

    }


    ////GETS
    @Test
    public void getPlayerTest(){

        when(playerRepository.existsById(1L)).thenReturn(true);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerTest));

        Player playerFound = playerService.getOnePlayerByID(1L);

        assertEquals(playerTest,playerFound);
        verify(playerRepository, times(1)).findById(playerFound.getId());

    }

    @Test
    public void getPlayerByUserNameTest(){

        when(playerRepository.existsByUsername(playerTest.getUsername())).thenReturn(true);
        when(playerRepository.getPlayerByUsername(playerTest.getUsername())).thenReturn(Optional.of(playerTest));

        Player playerFound;
        playerFound = playerService.getOnePlayerByUsername(playerTest.getUsername());

        assertEquals(playerTest, playerFound);
        verify(playerRepository, times(1)).getPlayerByUsername(playerFound.getUsername());
    }

    @Test
    public void updatePlayerTest() {

        when(playerRepository.existsById(playerTest.getId())).thenReturn(true);
        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));
        when(playerRepository.existsByEmail(playerDtoTest.getEmail())).thenReturn(false);
        when(playerRepository.existsByUsername(playerDtoTest.getUsername())).thenReturn(false);
        when(mapping.mapUpdate(playerDtoTest, playerTest)).thenReturn(playerTest);
        when(playerRepository.save(any(Player.class))).thenReturn(playerTest);

        Player playerResult = playerService.updatePlayer(playerDtoTest, playerTest.getId());

        assertEquals(playerResult.getUsername(), playerTest.getUsername());
        assertEquals(playerResult.getEmail(), playerTest.getEmail());
        assertEquals(playerResult.getPassword(), playerTest.getPassword());

        verify(playerRepository, times(1)).save(playerTest);

    }

    @Test
    public void deletePlayer(){

        //simular comportamiento de un un usuario que no es admin (no debería poder borrar)
        when(playerRepository.existsById(playerNotAdmin.getId())).thenReturn(true);
        when(playerRepository.findById(playerNotAdmin.getId())).thenReturn(Optional.of(playerNotAdmin));

        //comprobar que el usuario existe y tiene rol de usuario
        when(playerRepository.existsById(playerToDelete.getId())).thenReturn(true);
        assertTrue(playerRepository.existsById(playerToDelete.getId()));
        assertTrue(playerNotAdmin.getRoles().contains(roleUser));

        // definimos a un usuario como admin para que pueda borrar
        playerTest.addRole(roleAdmin);
        assertTrue(playerTest.getRoles().contains(roleAdmin));

        //simulamos comportamientos necesarios del metodo deletePlayer (admin y usuario a borrar)
        when(playerRepository.existsById(playerTest.getId())).thenReturn(true);
        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));

        when(playerRepository.existsById(playerToDelete.getId())).thenReturn(true);
        when(playerRepository.findById(playerToDelete.getId())).thenReturn(Optional.of(playerToDelete));

        playerService.deletePlayer(playerTest.getId(), playerToDelete.getId());

        //comprobar que se ha borrado el usuario
        when(playerRepository.existsById(playerToDelete.getId())).thenReturn(false);
        assertFalse(playerRepository.existsById(playerToDelete.getId()));

        //comprobar que si no es un admin no puede eliminar a otro usuario
        assertThrows(RolNotFoundException.class, () -> {
            playerService.deletePlayer(playerNotAdmin.getId(), playerToDelete.getId());
        });
    }

    //// ROLES
    @Test
    public void addRoleToPlayerTest(){


        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));
        when(rolesRepository.findRoleByName(roleUser.getName())).thenReturn(Optional.of(roleUser));
        when(playerRepository.save(any(Player.class))).thenReturn(playerTest);

        assertTrue(playerTest.getRoles().isEmpty());

        playerService.addRoleToPlayer(roleUser.getName(), playerTest.getId());

        assertTrue(playerTest.getRoles().contains(roleUser));


        verify(playerRepository, times(1)).save(playerTest);

    }

    @Test
    public void addRoleToPlayerTest_AdminAlreadyExists(){

        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));
        when(rolesRepository.findRoleByName(roleAdmin.getName())).thenReturn(Optional.of(roleAdmin));
        when(rolesRepository.existsByRoleName("admin")).thenReturn(true);

        assertThrows(AdminAlreadyExistsException.class, () -> {
            playerService.addRoleToPlayer(roleAdmin.getName(), playerTest.getId());
        });

    }


    ////DADOS
    @Test
    public void playerThrowDiceTest(){

        when(playerRepository.existsById(playerTest.getId())).thenReturn(true);
        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));
        when(gameUtilities.LaunchDice()).thenReturn(diceTest);
        when(diceRepository.save(any(Dice.class))).thenReturn(diceTest);

        Dice diceResult = playerService.playerThrowDice(playerTest.getId());

        assertNotNull(diceResult);
        assertEquals(diceTest, diceResult);

        verify(playerRepository).existsById(playerTest.getId());
        verify(playerRepository).findById(playerTest.getId());
        verify(gameUtilities).LaunchDice();
        verify(diceRepository).save(diceTest);

    }

    //FALTA ELIMINAR TIRADAS


}
