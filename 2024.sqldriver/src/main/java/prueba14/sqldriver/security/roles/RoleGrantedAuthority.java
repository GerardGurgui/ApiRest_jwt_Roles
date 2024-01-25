package prueba14.sqldriver.security.roles;

import org.springframework.security.core.GrantedAuthority;

public class RoleGrantedAuthority implements GrantedAuthority {

    private final String role;

    public RoleGrantedAuthority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {

        return role;

    }
}
