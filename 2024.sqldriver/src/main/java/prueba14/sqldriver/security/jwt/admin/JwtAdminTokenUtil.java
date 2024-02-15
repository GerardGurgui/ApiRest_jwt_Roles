package prueba14.sqldriver.security.jwt.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import prueba14.sqldriver.security.jwt.JwtTokenUtil;
import prueba14.sqldriver.security.service.admin.AdminDetailsServiceImpl;


@Component
public class JwtAdminTokenUtil {

    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String generateTokenForAdmin() {

        UserDetails userDetails = adminDetailsService.loadUserByUsername("admin");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return jwtTokenUtil.generateToken(authentication);

    }
}
