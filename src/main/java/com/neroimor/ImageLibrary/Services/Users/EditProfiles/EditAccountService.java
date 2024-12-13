package com.neroimor.ImageLibrary.Services.Users.EditProfiles;

import com.neroimor.ImageLibrary.Components.CheckUser.UserVerification;
import com.neroimor.ImageLibrary.Components.ErrorComoponent.DataError;
import com.neroimor.ImageLibrary.Components.Password.PasswordComponent;
import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import com.neroimor.ImageLibrary.Components.Properties.EditorUser;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class EditAccountService {
    private final UserRepository userRepository;
    private final UserVerification userVerification;
    private final DataError dataError;
    private final EditorUser editorUser;
    private final PasswordComponent passwordComponent;
    private final PasswordEncoder passwordEncoder;
    private final AppSettings appSettings;

    @Autowired
    public EditAccountService(UserRepository userRepository,
                              UserVerification userVerification,
                              DataError dataError,
                              EditorUser editorUser,
                              PasswordComponent passwordComponent, PasswordEncoder passwordEncoder,
                              AppSettings appSettings) {
        this.userRepository = userRepository;
        this.userVerification = userVerification;
        this.dataError = dataError;
        this.editorUser = editorUser;
        this.passwordComponent = passwordComponent;
        this.passwordEncoder = passwordEncoder;
        this.appSettings = appSettings;
    }

    public ResponseEntity<String> editNickname(String nickname) {
        String currentUsername = userVerification.getCurrentUsername();
        try {
            var user = userRepository.findByEmail(currentUsername);
            log.info("Обработка пользователя (изменение никнейма)");
            if (user.isPresent()) {
                if (!user.get().isVerified()) {
                    return errorVerification(user);
                }
                log.info("Пользователь присутсвует");
                user.get().setNickname(nickname);
                userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(editorUser.getSettingProfile().getRenickname());
            } else {
                log.info("Пользователь не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(editorUser.getSettingProfile().getUserError());
            }
        } catch (DataAccessException e) {
            return dataError.dataAccessError(e);
        }
    }

    public ResponseEntity<String> editPassword(String newPassword, String oldPassword) {
        String currentUsername = userVerification.getCurrentUsername();
        try {
            var user = userRepository.findByEmail(currentUsername);
            log.info("Обработка пользователя (изменение пароля)");
            if (user.isPresent()) {
                if (!user.get().isVerified()) {
                    return errorVerification(user);
                }
                if (passwordComponent.checkPassword(newPassword, user.get().getPassword())) {
                    log.info("Пороль верный, начата смена пороля");
                    String finalPassword = passwordEncoder.encode(
                            newPassword + appSettings.getSettingUsers().getSalt());
                    user.get().setPassword(finalPassword);
                    userRepository.save(user.get());
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(editorUser.getSettingProfile().getRepassword());
                } else {
                    log.info("Пороль не верный");
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(editorUser.getSettingProfile().getPasswordError());
                }
            } else {
                log.info("Пользователь не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(editorUser.getSettingProfile().getUserError());
            }

        } catch (DataAccessException e) {
            return dataError.dataAccessError(e);
        }

    }

    private ResponseEntity<String> errorVerification(Optional<User> user) {
            log.info("Пользователь не верефицирован");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(editorUser.getSettingProfile().getErrorVerification());
    }
}
