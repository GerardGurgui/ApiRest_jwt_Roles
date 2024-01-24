package prueba14.sqldriver.entities;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "RegistroTiradas")
public class Dice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dado1;
    private int dado2;
    private int resultadoTirada;

    public Dice() {
    }

    public int SumarDados() {
        return dado1 + dado2;
    }

}
