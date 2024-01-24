package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.repository.PlayerRepository;
import prueba14.sqldriver.security.jwt.JwtTokenUtil;
import prueba14.sqldriver.security.payload.JwtResponse;
import prueba14.sqldriver.security.payload.MessageResponse;
import prueba14.sqldriver.service.PlayerService;



/**
 * Controlador para llevar a cabo la autenticación utilizando JWT
 *
 * Se utiliza AuthenticationManager para autenticar las credenciales que son el
 * usuario y password que llegan por POST en el cuerpo de la petición
 *
 * Si las credenciales son válidas se genera un token JWT como respuesta
 *
 * ADAPTAR REGISTRO Y LOGIN A LA ENTIDAD DE JUGADORES, SE REGISTRAN Y LOGIN PARA PODER JUGAR CON LA COOKIE GENERADA
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

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody PlayerDto playerDTO){

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(playerDTO.getUsername(), playerDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken(authentication);

        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody PlayerDto playerDto) {

        // Check 1: username
        if (playerRepository.existsByUsername(playerDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check 2: email
        if (playerRepository.existsByEmail(playerDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account -- ADAPTAR A MI ENTIDAD JUGADOR

        playerService.createPlayer(playerDto);

        return ResponseEntity.ok(new MessageResponse("Player registered successfully!"));
    }
}
