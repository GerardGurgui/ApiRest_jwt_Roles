package prueba14.sqldriver.entities;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "registro_tiradas")
public class Dice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer dado1 = 0;
    private Integer dado2 = 0;
    private Integer resultadoTirada = 0;

    @Column(name = "id_player")
    private Long idPlayer;

    public Dice() {
    }

    public int SumarDados() {
        return dado1 + dado2;
    }

}
