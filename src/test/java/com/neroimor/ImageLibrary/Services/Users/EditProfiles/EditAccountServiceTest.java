package com.neroimor.ImageLibrary.Services.Users.EditProfiles;

import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import com.neroimor.ImageLibrary.Components.Properties.EditorUser;
import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import com.neroimor.ImageLibrary.Services.Users.Security.RegisterUserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class EditAccountServiceTest {

    private static final Logger log = LoggerFactory.getLogger(EditAccountServiceTest.class);
    @Autowired
    private RegisterUserService registerUserService;
    @Autowired
    private AppSettings appSettings;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EditorUser editorUser;
    @Autowired
    private EditAccountService editAccountService;

    private RegisterUser fishUser() {
        var fishUser = new RegisterUser();
        fishUser.setEmail("fish@fishmail.fish");
        fishUser.setPassword("fishfish");
        fishUser.setRepitePassword("fishfish");
        fishUser.setNickname("Fish");
        return fishUser;
    }

    private RegisterUser userAddedVerif(){
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isEmpty())
            fail();
        user.get().setVerified(true);
        userRepository.save(user.get());
        return fishUser;
    }
    private RegisterUser userAddedNotVerif(){
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        var user = userRepository.findByEmail(fishUser.getEmail());
        if (user.isEmpty())
            fail();
        return fishUser;
    }
    @Test
    public void testEditNickname(){
      var user =  userAddedVerif();
      var userCheck = userRepository.findByEmail(user.getEmail());
      log.info(userCheck.toString());
      ResponseEntity result = editAccountService.renickname("Lolo",user.getEmail());
      assertEquals(editorUser.getSettingProfile().getRenickname(),
              result.getBody());
      assertEquals(userRepository.findByEmail(user.getEmail()).get().getNickname(),
              "Lolo");
    }

    @Test
    public void testEditNicknameNotVerif(){
        var user =  userAddedNotVerif();
        var userCheck = userRepository.findByEmail(user.getEmail());
        log.info(userCheck.toString());
        ResponseEntity result = editAccountService.renickname("Lolo",user.getEmail());
        assertEquals(editorUser.getSettingProfile().getErrorVerification(),
                result.getBody());
    }

    @Test
    public void testEditPassword(){
        var user =  userAddedVerif();
        var userCheck = userRepository.findByEmail(user.getEmail());
        log.info(userCheck.toString());
        ResponseEntity result = editAccountService
                .repassword("lolololo","fishfish",user.getEmail());
        assertEquals(editorUser.getSettingProfile().getRepassword(),
                result.getBody());
    }

    @Test
    public void testEditPasswordNotCorrect(){
        var user =  userAddedVerif();
        var userCheck = userRepository.findByEmail(user.getEmail());
        log.info(userCheck.toString());
        ResponseEntity result = editAccountService
                .repassword("lolololo","fish",user.getEmail());
        assertEquals(editorUser.getSettingProfile().getPasswordError(),
                result.getBody());
    }

}