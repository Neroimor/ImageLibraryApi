package com.neroimor.ImageLibrary.Controllers.Management;


import com.neroimor.ImageLibrary.Components.JWTComponent.JwtTokenProvider;
import com.neroimor.ImageLibrary.Models.UsersModels.LoginData;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import com.neroimor.ImageLibrary.Services.Users.LoginService;
import com.neroimor.ImageLibrary.Services.Users.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserLoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserLoginController (LoginService loginService, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginData loginData) {
        return loginService.login(loginData.getEmail(), loginData.getPassword());
    }
}
