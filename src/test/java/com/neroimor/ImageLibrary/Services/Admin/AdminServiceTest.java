package com.neroimor.ImageLibrary.Services.Admin;

import com.neroimor.ImageLibrary.Components.Properties.AdminSettings;
import com.neroimor.ImageLibrary.Models.UsersModels.ChangeDataUser;
import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import com.neroimor.ImageLibrary.Services.Users.Security.RegisterUserService;
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
class AdminServiceTest {

    @Autowired
    private RegisterUserService registerUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminSettings adminSettings;
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

    private User registerFishUser() {
        var fishUser = fishUser();
        registerUserService.registerUser(fishUser);
        return userRepository.findByEmail(fishUser.getEmail()).get();
    }

    @Test
    public void testDeletedUserIsPresent() {
        var user = registerFishUser();
        ResponseEntity<String> result = adminService.deleteUser(user.getEmail());
        assertEquals(adminSettings
                .getSettingOperation()
                .getDeleted(), result.getBody());
    }

    @Test
    public void testDeletedUserIsEmpty() {
        ResponseEntity<String> result = adminService.deleteUser("");
        assertEquals(adminSettings
                .getSettingOperation()
                .getUserNotFound(), result.getBody());
    }

    @Test
    public void testEditUserIsPresent() {
        var user = registerFishUser();
        var editUserFish = new ChangeDataUser();
        editUserFish.setNickname("Pupkin");
        editUserFish.setPassword("Aranold!Cat");
        ResponseEntity<String> result = adminService.editUserData(editUserFish, user.getEmail());
        assertEquals(adminSettings.getSettingOperation().getEdit(), result.getBody());
        var userFish = userRepository.findByEmail(user.getEmail()).get();
        assertEquals(editUserFish.getNickname(), userFish.getNickname());
    }

    @Test
    public void testEditUserIsEmpty() {
        var editUserFish = new ChangeDataUser();
        editUserFish.setNickname("Pupkin");
        editUserFish.setPassword("Aranold!Cat");
        ResponseEntity<String> result = adminService.editUserData(editUserFish, "");
        assertEquals(adminSettings
                .getSettingOperation()
                .getUserNotFound(), result.getBody());
    }

    @Test
    public void testChangeRole(){
        var user = registerFishUser();
        ResponseEntity<String> result =  adminService.userToAdmin(user.getEmail());
        assertEquals(adminSettings.getSettingRole().getUserChangeRole()+
                adminSettings.getSettingRole().getRoleAdmin(), result.getBody());
        result = adminService.adminToUser(user.getEmail());
        assertEquals(adminSettings.getSettingRole().getUserChangeRole()+
                adminSettings.getSettingRole().getRoleUser(), result.getBody());
    }


}