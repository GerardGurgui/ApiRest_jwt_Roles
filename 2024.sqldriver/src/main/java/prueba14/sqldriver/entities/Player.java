package prueba14.sqldriver.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import prueba14.sqldriver.exceptions.PlayerHaveRoleException;
import prueba14.sqldriver.exceptions.PlayerNotFoundException;

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

    private Integer puntuacion = 0;
    private Integer winner = 0;

    @Column(name = "porcentaje_acierto")
    private Integer acierto = 0;

    @Column(name = "login_date")
    private LocalDate loginDate;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "id_player", referencedColumnName = "id")
    private Set<Dice> throwsDices;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "player_roles",
            joinColumns = @JoinColumn(name = "id_player", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
    @JsonManagedReference // manejar serializacion de los jugadores y no entrar en bucle
    private Set<Roles> roles;


    public Player() {
    }

    public Player(Long id, String username, String email, String password, int puntuacion, int winner) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.puntuacion = puntuacion;
        this.winner = winner;
    }

    ///////TIRADAS DE DADOS
    public void addThrow(Dice tirada){

        if (throwsDices == null){
            throwsDices = new HashSet<>();
        }

        throwsDices.add(tirada);
    }

    //////ROLES

    public void addRole(Roles role){

        if (hasRole()){

            throw new PlayerHaveRoleException(HttpStatus.CONFLICT,"Player already have role");
        }

        if (roles == null){
            roles = new HashSet<>();
        }

        roles.add(role);
    }

    public boolean hasRole(){

        return roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("USER")
                || role.getName().equalsIgnoreCase("ADMIN"));

    }

    public boolean isAdmin(){

        return roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));
    }

}
