package prueba14.sqldriver.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Player;

import java.time.LocalDate;

@Component
public class Map implements Imapper<PlayerDto, Player> {


    private PasswordEncoder encoder;

    @Autowired
    public Map(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public Player map(PlayerDto playerDto) {

        Player player = new Player();

        if (playerDto.getUsername().isEmpty()) {

            player.setUsername("Anonimo");

        } else {

            player.setUsername(playerDto.getUsername());
        }

        player.setEmail(playerDto.getEmail());
        player.setPassword(encoder.encode(playerDto.getPassword()));
        player.setLoginDate(LocalDate.now());

        return player;

    }

    @Override
    public Player mapUpdate(PlayerDto playerDto, Player playerToUpdate) {


        if (playerDto.getUsername() != null) {

            playerToUpdate.setUsername(playerDto.getUsername());
        }

        if (playerDto.getEmail() != null) {

            playerToUpdate.setEmail(playerDto.getEmail());
        }

        if (playerDto.getPassword() != null) {

            playerToUpdate.setPassword(encoder.encode(playerDto.getPassword()));
        }

        return playerToUpdate;

    }

}
