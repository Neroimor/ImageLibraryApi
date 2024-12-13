package com.neroimor.ImageLibrary.Controllers.Profile;

import com.neroimor.ImageLibrary.Components.CheckUser.UserVerification;
import com.neroimor.ImageLibrary.Services.Users.EditProfiles.EditAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class EditProfileUserController {

    private final EditAccountService editAccountService;


    @Autowired
    public EditProfileUserController(EditAccountService editAccountService, UserVerification userVerification) {
        this.editAccountService = editAccountService;
    }

    @PutMapping("/edit/{nickname}")
    public ResponseEntity<String> editProfileNickname(@PathVariable String nickname) {
        log.info("Начата смена nickname");
        // Возвращаем email пользователя, а не строку "currentUsername"
        return editAccountService.editNickname(nickname);
    }
    @PutMapping("/edit/password")
    public ResponseEntity<String> editProfilePassword(@RequestParam String newPassword, @RequestParam String oldPassword) {
        log.info("Начата смена пароля");
        // Возвращаем email пользователя, а не строку "currentUsername"
        return editAccountService.editPassword(newPassword, oldPassword);
    }
}
