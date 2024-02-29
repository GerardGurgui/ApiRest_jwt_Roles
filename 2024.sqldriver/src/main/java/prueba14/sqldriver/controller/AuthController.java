package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.security.payload.JwtResponse;
import prueba14.sqldriver.security.payload.MessageResponse;
import prueba14.sqldriver.service.AdminService;
import prueba14.sqldriver.service.AuthService;
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
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody PlayerDto playerDTO){

        return new ResponseEntity<>(authService.login(playerDTO), HttpStatus.OK);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<MessageResponse> registerAdmin(@Valid @RequestBody PlayerDto playerDto) {

       authService.registerAdmin(playerDto);

       return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody PlayerDto playerDto) {

        authService.register(playerDto);

        return ResponseEntity.ok(new MessageResponse("Player registered successfully!"));
    }


}
