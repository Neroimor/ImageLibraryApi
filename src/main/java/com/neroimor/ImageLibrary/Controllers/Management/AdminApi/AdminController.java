package com.neroimor.ImageLibrary.Controllers.Management.AdminApi;

import com.neroimor.ImageLibrary.Models.UsersModels.ChangeDataUser;
import com.neroimor.ImageLibrary.Services.Admin.AdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deletedUser(@RequestParam String email) {
        log.info("Начато удалегние пользователя");
        return adminService.deleteUser(email);
    }

    @PutMapping("/editUserData/{email}")
    public ResponseEntity<String> editUser(@Valid @RequestBody ChangeDataUser changeDataUser, @PathVariable String email) {
        log.info("Начато редактирование пользователя");
        return adminService.editUserData(changeDataUser,email);
    }

    @PutMapping("/ToAdmin")
    public ResponseEntity<String> changeRoleToAdmin(@RequestParam String email) {
        log.info("Начато изменение роли пользователя с пользователя на админа");
        return adminService.userToAdmin(email);
    }

    @PutMapping("/ToUser")
    public ResponseEntity<String> changeRoleToUser(@RequestParam String email) {
        log.info("Начато изменение роли пользователя с админа на пользователя");
        return adminService.adminToUser(email);
    }
}
