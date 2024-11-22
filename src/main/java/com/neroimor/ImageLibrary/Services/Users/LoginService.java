package com.neroimor.ImageLibrary.Services.Users;

import com.neroimor.ImageLibrary.Components.ErrorComoponent.DataError;
import com.neroimor.ImageLibrary.Components.JWTComponent.JwtTokenProvider;
import com.neroimor.ImageLibrary.Components.Password.PasswordComponent;
import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import com.neroimor.ImageLibrary.Components.UID.GeneratorUID;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LoginService {

    private final DataError dataError;
    private final AppSettings appSettings;
    private final GeneratorUID generatorUID;
    private final UserRepository userRepository;
    private final PasswordComponent passwordComponent;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginService(DataError dataError,
                        AppSettings appSettings,
                        GeneratorUID generatorUID,
                        UserRepository userRepository,
                        PasswordComponent passwordComponent,
                        JwtTokenProvider jwtTokenProvider) {
        this.dataError = dataError;
        this.appSettings = appSettings;
        this.generatorUID = generatorUID;
        this.userRepository = userRepository;
        this.passwordComponent = passwordComponent;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public ResponseEntity<String> login(String email, String password){
        try {
            Optional<User> user = userRepository.findByEmail(email);
            log.info("Начинается процесс аунтификации");
            return Authentication(user,password);
        } catch (DataAccessException e){
            return dataError.dataAccessError(e);
        }
    }

    private ResponseEntity<String> Authentication(Optional<User> user,String password){
        if(user.isPresent()){
            if(passwordComponent.checkPassword(user.get().getPassword(),password)){
                log.info("Пользователь вошел");
             String token = jwtTokenProvider
                     .generateToken(user.get().getEmail(),user.get().getROLE());
             ResponseEntity.status(HttpStatus.OK).body(token);
            }
        }

        return null;
    }
}
