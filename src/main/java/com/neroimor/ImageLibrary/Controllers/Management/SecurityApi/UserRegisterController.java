package com.neroimor.ImageLibrary.Controllers.Management.SecurityApi;

import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Services.Users.Security.RegisterUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reg")
@Slf4j
public class UserRegisterController {

    private final RegisterUserService userService;

    @Autowired
    public UserRegisterController(RegisterUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUsers(@Valid @RequestBody RegisterUser registerUser) {
        log.info("Начата регистрация нового пользователя");
        return userService.registerUser(registerUser);
    }

    @PostMapping("/register/verification")
    public ResponseEntity<String> verificationUsers(@RequestBody Map<String, String> dataUser) {
        log.info("Начата верификация пользователя");
        return userService.userVerified(dataUser.get("email"), dataUser.get("uid"));
    }

}
