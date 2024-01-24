package prueba14.sqldriver.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDto {

    //ATRIBUTOS QUE INTRODUCIRÁ EL USUARIO, EL SERVICE PROGRAMAREMOS DE TRANSFORMAR Y PERSISTIR EN BDD COMO UNA ENTIDAD
    private String username;
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
