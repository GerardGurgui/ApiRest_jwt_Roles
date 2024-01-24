package prueba14.sqldriver.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Player;

import java.time.LocalDate;

@Component
public class Map implements Imapper<PlayerDto, Player> {

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Player map(PlayerDto playerDto) {

        Player player = new Player();

        if (playerDto.getUsername().isEmpty()){

            player.setUsername("Anonimo");

        } else {

            player.setUsername(playerDto.getUsername());
        }

        player.setEmail(playerDto.getEmail());
        player.setPassword(encoder.encode(playerDto.getPassword()));
        player.setFechaRegistro(LocalDate.now());

        return player;

    }
}
