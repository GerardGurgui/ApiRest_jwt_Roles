package prueba14.sqldriver.servicesTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.security.jwt.JwtTokenUtil;
import prueba14.sqldriver.security.payload.JwtResponse;
import prueba14.sqldriver.security.payload.MessageResponse;
import prueba14.sqldriver.service.AdminService;
import prueba14.sqldriver.service.AuthService;
import prueba14.sqldriver.service.PlayerService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerService playerService;
    @Mock
    private AdminService adminService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private static PlayerDto playerDto = new PlayerDto();

    @BeforeAll
    public static void setUp() {

        playerDto.setUsername("playerDto");
        playerDto.setPassword("playerDto");
        playerDto.setEmail("playerDto@test.com");
    }


    @Test
    public void loginTest() {

        Authentication auth = mock(Authentication.class);

        when(authManager.authenticate(new UsernamePasswordAuthenticationToken(playerDto.getUsername(), playerDto.getPassword()))).thenReturn(auth);
        when(jwtTokenUtil.generateToken(auth)).thenReturn("token");

        JwtResponse jwtResponse = authService.login(playerDto);

        assertEquals("token", jwtResponse.getToken());

    }


    @Test
    public void registerTest() {

        when(playerRepository.existsByUsername(playerDto.getUsername())).thenReturn(false);
        when(playerRepository.existsByEmail(playerDto.getEmail())).thenReturn(false);

        ResponseEntity<MessageResponse> response = authService.register(playerDto);

        assertEquals("User registered successfully!", response.getBody().getMessage());

        when(playerRepository.existsByUsername(playerDto.getUsername())).thenReturn(true);
        assertTrue(playerRepository.existsByUsername(playerDto.getUsername()));
    }


}
