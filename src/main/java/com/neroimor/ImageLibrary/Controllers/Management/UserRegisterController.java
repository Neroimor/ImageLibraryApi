package com.neroimor.ImageLibrary.Controllers.Management;

import com.neroimor.ImageLibrary.Components.JWTComponent.JwtTokenProvider;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import com.neroimor.ImageLibrary.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserRegisterController {

    private final UserService userService;

    @Autowired
    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        userService.registerUser(email, password);
        return "User registered successfully";
    }


    //To do testing
    @GetMapping("user")
    public String hello(){
        return "Hello World";
    }
}
