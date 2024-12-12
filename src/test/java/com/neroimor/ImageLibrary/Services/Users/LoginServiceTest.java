package com.neroimor.ImageLibrary.Services.Users;

import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import com.neroimor.ImageLibrary.Services.Users.Security.LoginService;
import com.neroimor.ImageLibrary.Services.Users.Security.RegisterUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterUserService registerUserService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppSettings appSettings;

    @Test
    public void testLoginCorrectPassword() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isEmpty())
            fail();
        registerUserService.userVerified(user.get().getEmail(), user.get().getCodeuid());
        ResponseEntity<String> responseResult = loginService.login(fishUser.getEmail(), fishUser.getPassword());
        assertEquals(responseResult.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testLoginCorrectPasswordNotVerified() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isEmpty())
            fail();
        ResponseEntity<String> responseResult = loginService.login(fishUser.getEmail(), fishUser.getPassword());
        assertEquals(appSettings.getLoginMoment().getUserNotVerified(), responseResult.getBody());
    }

    @Test
    public void testLoginCorrectPasswordNotCorrectPassword() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isEmpty())
            fail();
        registerUserService.userVerified(user.get().getEmail(), user.get().getCodeuid());
        fishUser.setPassword("assdasdsdadaasd");
        ResponseEntity<String> responseResult = loginService.login(fishUser.getEmail(), fishUser.getPassword());
        assertEquals(appSettings.getLoginMoment().getUnauthorized(), responseResult.getBody());
    }

    @Test
    public void testLoginCorrectPasswordNotRegister() {
        var fishUser = fishUser();
        ResponseEntity<String> responseResult = loginService.login(fishUser.getEmail(), fishUser.getPassword());
        assertEquals(appSettings.getLoginMoment().getUserNotFound(), responseResult.getBody());
    }

    private RegisterUser fishUser() {
        var fishUser = new RegisterUser();
        fishUser.setEmail("fish@fishmail.fish");
        fishUser.setPassword("fish");
        fishUser.setRepitePassword("fish");
        fishUser.setNickname("Fish");
        return fishUser;
    }
}