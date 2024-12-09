package com.neroimor.ImageLibrary.Services.Users;

import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class RegisterUserServiceTest {
    @Autowired
    private RegisterUserService registerUserService;
    @Autowired
    private AppSettings appSettings;
    @Autowired
    private UserRepository userRepository;

    private RegisterUser fishUser() {
        var fishUser = new RegisterUser();
        fishUser.setEmail("fish@fishmail.fish");
        fishUser.setPassword("fishfish");
        fishUser.setRepitePassword("fishfish");
        fishUser.setNickname("Fish");
        return fishUser;
    }

    @Test
    public void testRegisterUserNotPresentUserAndCorrectPassword() {
        var fishUser = fishUser();
        ResponseEntity<String> responseResult = registerUserService.registerUser(fishUser);
        assertEquals(responseResult.getBody(), appSettings.getRegisterResponse().getCreatedUser());
    }

    @Test
    public void testRegisterUserIsPresentAndNotVerefiedAndCorrectPassword() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        ResponseEntity<String> responseResult = registerUserService.registerUser(fishUser);
        assertEquals(responseResult.getBody(), appSettings.getRegisterResponse().getUserFoundAndNotVerified());
    }

    @Test
    public void testRegisterUserPasswordDontMatch() {
        var fishUser = fishUser();
        fishUser.setPassword("fishfishfish");
        ResponseEntity<String> responseResult = registerUserService.registerUser(fishUser);
        assertEquals(responseResult.getBody(), appSettings.getRegisterResponse().getPasswordsDontMatch());
    }

    @Test
    public void testRegisterUserIsPresentAndNotVerefiedAndNotCorrectPassword() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        fishUser.setPassword("fishfishfish");
        fishUser.setRepitePassword("fishfishfish");
        ResponseEntity<String> responseResult = registerUserService.registerUser(fishUser);
        assertEquals(responseResult.getBody(), appSettings.getErrorResponseServer().getUserPasswordNotCorrect());
    }

    @Test
    public void testRegisterUserIsPresentAndYesVerifiedAndCorrectPassword() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isPresent()) {
            user.get().setVerified(true);
            userRepository.save(user.get());
            ResponseEntity<String> responseResult = registerUserService.registerUser(fishUser);
            assertEquals(responseResult.getBody(), appSettings.getRegisterResponse().getUserFound());
        } else
            fail();
    }

    @Test
    public void testUserVerifiedCorrectUid() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isEmpty())
            fail();
        ResponseEntity<String> responseResult = registerUserService
                .userVerified(user.get().getEmail(), user.get().getCodeuid());
        assertEquals(responseResult.getBody(), appSettings.getRegisterResponse().getUserVerified());
    }

    @Test
    public void testUserVerifiedNotCorrectUid() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isEmpty())
            fail();
        ResponseEntity<String> responseResult = registerUserService
                .userVerified(user.get().getEmail(), "AAAAAAAAA");
        assertEquals(responseResult.getBody(), appSettings.getRegisterResponse().getErrorUid());
    }


    @Test
    public void testUserVerifiedUserNotFound() {
        var fishUser = fishUser();
        ResponseEntity<String> responseResult = registerUserService
                .userVerified(fishUser.getEmail(), "AAAAAAAAA");
        assertEquals(responseResult.getBody(), appSettings.getErrorResponseServer().getUserNotFound());
    }

}