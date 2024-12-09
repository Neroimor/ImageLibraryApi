package com.neroimor.ImageLibrary.Services.Admin;

import com.neroimor.ImageLibrary.Components.ErrorComoponent.DataError;
import com.neroimor.ImageLibrary.Components.Properties.AdminSettings;
import com.neroimor.ImageLibrary.Models.UsersModels.ChangeDataUser;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AdminSettings adminSettings;
    private final DataError dataError;
    @Autowired
    public AdminService(UserRepository userRepository, UserRepository userRepository1, AdminSettings adminSettings, DataError dataError) {
        this.userRepository = userRepository1;
        this.adminSettings = adminSettings;
        this.dataError = dataError;
    }

    public ResponseEntity<String> deleteUser(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                userRepository.delete(user.get());
                return ResponseEntity.status(HttpStatus.OK).body(adminSettings.getSettingOperation().getDeleted());
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(adminSettings.getSettingOperation().getUserNotFound());
            }
        } catch (DataAccessException e) {
          return dataError.dataAccessError(e);
        }
    }

    public ResponseEntity<String> editUserData(ChangeDataUser changeDataUser, String email){
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                var userNew = user.get();
                editUser(changeDataUser, userNew);
                return ResponseEntity.status(HttpStatus.OK).body(adminSettings.getSettingOperation().getEdit());
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(adminSettings.getSettingOperation().getUserNotFound());
            }
        } catch (DataAccessException e) {
            return dataError.dataAccessError(e);
        }
    }
    private void editUser(ChangeDataUser changeDataUser, User user){
        user.setNickname(changeDataUser.getNickname());
        user.setPassword(changeDataUser.getPassword());
    }


}
