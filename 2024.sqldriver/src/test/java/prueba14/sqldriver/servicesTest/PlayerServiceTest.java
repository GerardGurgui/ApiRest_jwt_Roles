package prueba14.sqldriver.servicesTest;


import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.transaction.annotation.Transactional;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.gameUtilities.GameUtilities;
import prueba14.sqldriver.mapper.Map;
import prueba14.sqldriver.repository.DiceRepository;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.service.PlayerService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private Map mapping;

    @Mock
    private GameUtilities gameUtilities;

    private Dice diceTest = new Dice();

    private Player playerTest = new Player();

    private PlayerDto playerDtoTest = new PlayerDto();




    @BeforeAll
    public void setUp(){

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        mapping = new Map(encoder);

        playerTest.setId(1L);
        playerTest.setUsername("playerTest");
        playerTest.setPassword("playerTest");
        playerTest.setEmail("playerTest");

        diceTest.setDado1(1);
        diceTest.setDado2(1);
        diceTest.setResultadoTirada(2);

        playerDtoTest.setUsername("playerDtoTest");
        playerDtoTest.setPassword("playerDtoTest");
        playerDtoTest.setEmail("playerDtoTest");

    }

    ////CREATE

    @Test
    public void createPlayerTest(){


        when(mapping.map(playerDtoTest)).thenReturn(playerTest);
        when(playerRepository.existsByUsername(playerDtoTest.getUsername())).thenReturn(false);
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
    public void updatePlayerTest(){

        when(playerRepository.existsById(playerTest.getId())).thenReturn(true);
        when(playerRepository.findById(playerTest.getId())).thenReturn(Optional.of(playerTest));
        when(playerRepository.existsByEmail(playerDtoTest.getEmail())).thenReturn(false);
        when(playerRepository.existsByUsername(playerDtoTest.getUsername())).thenReturn(false);
        when(mapping.mapUpdate(playerDtoTest, playerTest)).thenReturn(playerTest);
        when(playerRepository.save(any(Player.class))).thenReturn(playerTest);

        playerTest = playerService.updatePlayer(playerDtoTest, 1L);

        assertEquals(playerTest.getUsername(), playerDtoTest.getUsername());

        verify(playerRepository, times(1)).save(any(Player.class));

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


}
