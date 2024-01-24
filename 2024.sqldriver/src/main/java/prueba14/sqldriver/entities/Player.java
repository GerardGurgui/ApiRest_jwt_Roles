package prueba14.sqldriver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter

@Table(name = "Players")
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private int puntuacion;
    private int victoria;

    @Column(name = "porcentaje_acierto")
    private int acierto;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    //Lazy para las peticiones que le pedimos y no todo lo relacionado
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "id_player", referencedColumnName = "id")
    private Set<Dice> tiradas;

    public Player() {
    }

    public Player(Long id, String username, String email, String password, int puntuacion, int victoria) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.puntuacion = puntuacion;
        this.victoria = victoria;
    }

    public void addTirada(Dice tirada){

        if (tiradas == null){
            tiradas = new HashSet<>();
        }

        tiradas.add(tirada);
    }



}
