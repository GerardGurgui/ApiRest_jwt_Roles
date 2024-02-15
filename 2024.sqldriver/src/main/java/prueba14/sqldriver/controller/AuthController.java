package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.security.jwt.admin.JwtAdminTokenUtil;
import prueba14.sqldriver.security.jwt.JwtTokenUtil;
import prueba14.sqldriver.security.payload.JwtResponse;
import prueba14.sqldriver.security.payload.MessageResponse;
import prueba14.sqldriver.service.PlayerService;

import javax.validation.Valid;


/**
 * Controlador para llevar a cabo la autenticación utilizando JWT
 * Se utiliza AuthenticationManager para autenticar las credenciales que son el
 * usuario y password que llegan por POST en el cuerpo de la petición
 * Si las credenciales son válidas se genera un token JWT como respuesta
 */
// @CrossOrigin(origins = "http://localhost:8081")

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtAdminTokenUtil jwtAdminTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login/admin")
    public ResponseEntity<JwtResponse> loginAdmin(@RequestBody PlayerDto playerDTO){

        if (!playerDTO.getUsername().equals("admin")
            || !passwordEncoder.matches(playerDTO.getPassword(),passwordEncoder.encode("admin"))){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtAdmin = jwtAdminTokenUtil.generateTokenForAdmin();

        return ResponseEntity.ok(new JwtResponse(jwtAdmin));

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody PlayerDto playerDTO){

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(playerDTO.getUsername(), playerDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody PlayerDto playerDto) {

        if (playerRepository.existsByUsername(playerDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }

        if (playerRepository.existsByEmail(playerDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        playerService.createPlayer(playerDto);

        return ResponseEntity.ok(new MessageResponse("Player registered successfully!"));
    }


}
