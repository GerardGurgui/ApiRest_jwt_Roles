package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prueba14.sqldriver.service.AdminService;

@RestController
@RequestMapping("/roles")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{playerId}/{rolename}")
    public void modifyRole(@PathVariable Long playerId, @PathVariable String rolename) {

        adminService.modifyRole(playerId, rolename);

    }
}
