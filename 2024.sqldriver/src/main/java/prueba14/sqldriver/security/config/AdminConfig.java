package prueba14.sqldriver.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import prueba14.sqldriver.security.user.CustomUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AdminConfig {

    @Value("${admin.username}")
    private String username;
    @Value("${admin.password}")
    private String password;

    @Bean
    public CustomUserDetails superAdmin() {

        return new CustomUserDetails(
                username,
                new BCryptPasswordEncoder().encode(password),
                getAdminAuthorities(),
                1L
        );
    }

    private Collection<? extends GrantedAuthority> getAdminAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }
}