package prueba14.sqldriver.servicesTest;


import org.junit.Assert;
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

        //DADOS
        diceTest.setDado1(1);
        diceTest.setDado2(1);
        diceTest.setResultadoTirada(2);

        playerTest.addThrow(diceTest);

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

        playerTest.setRoles(roles);

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
        when(rolesRepository.findRoleByName("USER")).thenReturn(Optional.of(roleUser));

        Player playerResult = playerService.createPlayer(playerDtoTest);

        assertEquals(playerResult.getUsername(), playerTest.getUsername());
        assertEquals(playerResult.getEmail(), playerTest.getEmail());
        assertEquals(playerResult.getPassword(), playerTest.getPassword());
        assertEquals(playerResult.getRoles(), playerTest.getRoles());

        verify(playerRepository, times(1)).save(playerTest);

    }


    ////GET
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

    @Test
    public void deleteThrowsTest(){

        when(playerRepository.existsById(playerTest.getId())).thenReturn(true);
        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));

        assertFalse(playerTest.getThrowsDices().isEmpty());

        playerService.deleteThrows(playerTest.getId());

        assertTrue(playerTest.getThrowsDices().isEmpty());

    }

}
