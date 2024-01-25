package prueba14.sqldriver.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import prueba14.sqldriver.entities.Roles;
import prueba14.sqldriver.repository.RolesRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;

// Clase que inicializa los roles en la base de datos con los roles de dbRoles.properties
@Component
public class InitDbRole {

    @Autowired
    private RolesRepository rolesRepository;

    @Value("${roles}")
    private String[] roles;

    @PostConstruct
    public void initDb() {
        Arrays.stream(roles).forEach(role -> {
            if (rolesRepository.findByName(role).isEmpty()) {
                Roles newRole = new Roles();
                newRole.setName(role);
                rolesRepository.save(newRole);
            }
        });
    }
}
