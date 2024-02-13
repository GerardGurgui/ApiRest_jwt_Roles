package prueba14.sqldriver.DTO;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class PlayerDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "username must be between 3 and 20 characters")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "email must be a valid email")
    private String email;

    private String password;

    public PlayerDto() {
    }

    public PlayerDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
