package com.neroimor.ImageLibrary.Controllers.Management;

import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Services.Users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reg")
@Slf4j
public class UserRegisterController {

    private final UserService userService;

    @Autowired
    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUser registerUser) {
        log.info("Начата регистрация нового пользователя");
        return userService.registerUser(registerUser);
    }


    //To do testing
    @GetMapping("user")
    public String hello(){
        return "Hello World";
    }
}
