package prueba14.sqldriver.security.service.admin;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prueba14.sqldriver.security.user.CustomUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AdminDetailsServiceImpl implements IAdminDetailsService{

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!username.equals("admin")) {
            throw new UsernameNotFoundException("Admin " + username + " not found");
        }

        return new CustomUserDetails(
                "admin",
                "admin",
                getAdminAuthorities(),
                1L);
    }

    private Collection<? extends GrantedAuthority> getAdminAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("WRITE"));
        authorities.add(new SimpleGrantedAuthority("READ"));
        authorities.add(new SimpleGrantedAuthority("DELETE"));
        authorities.add(new SimpleGrantedAuthority("UPDATE"));
        return authorities;
    }
}
