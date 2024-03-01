package prueba14.sqldriver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.security.jwt.JwtTokenUtil;
import prueba14.sqldriver.security.payload.JwtResponse;
import prueba14.sqldriver.security.payload.MessageResponse;


@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public JwtResponse login(PlayerDto playerDTO){

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(playerDTO.getUsername(), playerDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken(authentication);

        return new JwtResponse(jwt);
    }

    public ResponseEntity<MessageResponse> register(PlayerDto playerDto) {

        checkUserNameAndEmail(playerDto);

        playerService.createPlayer(playerDto);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    private void checkUserNameAndEmail(PlayerDto playerDto) {

        if (playerRepository.existsByUsername(playerDto.getUsername())) {
            throw new RuntimeException("Error: Username is already taken");
        }

        if (playerRepository.existsByEmail(playerDto.getEmail())) {
            throw new RuntimeException("Error: Email is already in use");
        }
    }

}
