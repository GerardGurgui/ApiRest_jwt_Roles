package prueba14.sqldriver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba14.sqldriver.security.payload.MessageResponse;
import prueba14.sqldriver.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("addRole/{playerId}/{rolename}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> addRole(@PathVariable Long playerId, @PathVariable String rolename) {

        adminService.addRole(playerId, rolename);
        return ResponseEntity.ok(new MessageResponse("Role added successfully!"));
    }

    @DeleteMapping("/deleteUser/{idToDelete}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deletePlayer(@PathVariable Long idToDelete){

        adminService.deleteUser(idToDelete);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }

    @DeleteMapping("/deleteRole/{playerId}/{rolename}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteRole(@PathVariable Long playerId, @PathVariable String rolename){

        adminService.deleteRole(playerId, rolename);
        return ResponseEntity.ok(new MessageResponse("Role deleted successfully!"));
    }
}
