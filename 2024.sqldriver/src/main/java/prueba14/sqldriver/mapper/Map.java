package prueba14.sqldriver.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import prueba14.sqldriver.DTO.PlayerDto;
import prueba14.sqldriver.entities.Player;
import prueba14.sqldriver.exceptions.AdminAlreadyExistsException;
import prueba14.sqldriver.security.user.CustomUserDetails;

import java.time.LocalDate;

@Component
public class Map implements Imapper<PlayerDto, Player> {

    private PasswordEncoder encoder;
    @Autowired
    private CustomUserDetails superAdmin;

    @Autowired
    public Map(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public Player map(PlayerDto playerDto) {

        Player player = new Player();

        if (playerDto.getUsername().equalsIgnoreCase(superAdmin.getUsername())) {

            throw new AdminAlreadyExistsException(HttpStatus.CONFLICT,"Error: Username is already taken for admin");
        }

        if (playerDto.getUsername().isEmpty()) {

            player.setUsername("Anonymous");

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

            if (playerDto.getUsername().equalsIgnoreCase(superAdmin.getUsername())) {
                throw new AdminAlreadyExistsException(HttpStatus.CONFLICT,"Error: Username is already taken for admin");
            }

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
