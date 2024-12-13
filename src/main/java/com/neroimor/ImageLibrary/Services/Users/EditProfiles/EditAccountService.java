package com.neroimor.ImageLibrary.Services.Users.EditProfiles;

import com.neroimor.ImageLibrary.Components.CheckUser.UserVerification;
import com.neroimor.ImageLibrary.Components.ErrorComoponent.DataError;
import com.neroimor.ImageLibrary.Components.Properties.EditorUser;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EditAccountService {
    private final UserRepository userRepository;
    private final UserVerification userVerification;
    private final DataError dataError;
    private final EditorUser editorUser;

    @Autowired
    public EditAccountService(UserRepository userRepository, UserVerification userVerification, DataError dataError, EditorUser editorUser) {
        this.userRepository = userRepository;
        this.userVerification = userVerification;
        this.dataError = dataError;
        this.editorUser = editorUser;
    }

    public ResponseEntity<String> editNickname(String nickname) {
        String currentUsername = userVerification.getCurrentUsername();
        try {

            var user = userRepository.findByEmail(currentUsername);
            log.info("Обработка пользователя");
            return user.map(value -> ResponseEntity.status(HttpStatus.OK)
                            .body(editorUser.getSettingProfile().getRenickname()))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(editorUser.getSettingProfile().getRenickname()));
        } catch (DataAccessException e) {
            return dataError.dataAccessError(e);
        }

    }
}
