package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba14.sqldriver.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{playerId}/{rolename}")
    public void modifyRole(@PathVariable Long playerId, @PathVariable String rolename) {

        adminService.modifyRole(playerId, rolename);
    }

    @DeleteMapping("/deleteUser/{idToDelete}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePlayer(@PathVariable Long idToDelete){

        adminService.deletePlayer(idToDelete);
    }
}
