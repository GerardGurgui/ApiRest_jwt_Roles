package prueba14.sqldriver.roles;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:dbRoles.properties")
public class DbRolesConfig {

    // No se necesita ninguna lógica adicional aquí por ahora
}