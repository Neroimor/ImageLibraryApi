package com.neroimor.ImageLibrary.Controllers.Management;

import com.neroimor.ImageLibrary.Models.UsersModels.ChangeDataUser;
import com.neroimor.ImageLibrary.Services.Admin.AdminService;
import jakarta.validation.Valid;
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

    @PostMapping("/editUserData/{email}")
    public ResponseEntity<String> editUser(@Valid @RequestBody ChangeDataUser changeDataUser, @PathVariable String email) {
        return adminService.editUserData(changeDataUser,email);
    }

    @PostMapping
    public ResponseEntity<String> changeRoleToAdmin(@RequestParam String email) {
        return adminService.userToAdmin(email);
    }

    @PostMapping
    public ResponseEntity<String> changeRoleToUser(@RequestParam String email) {
        return adminService.adminToUser(email);
    }
}
