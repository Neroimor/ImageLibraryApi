package com.neroimor.ImageLibrary.Controllers.Management;

import com.neroimor.ImageLibrary.Services.Admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deletedUser(@RequestParam String email) {
        return adminService.deleteUser(email);
    }

    @PostMapping("/editUserData")
    public ResponseEntity<String> editUser() {
        return ResponseEntity.ok("Updated user");
    }

    @PostMapping
    public ResponseEntity<String> changeRoleToAdmin() {
        return ResponseEntity.ok("Change role to admin");
    }

    @PostMapping
    public ResponseEntity<String> changeRoleToUser() {
        return ResponseEntity.ok("Changed role to admin");
    }
}
