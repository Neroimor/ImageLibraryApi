package com.neroimor.ImageLibrary.Services.Users;

import com.neroimor.ImageLibrary.Components.ErrorComoponent.DataError;
import com.neroimor.ImageLibrary.Components.Password.PasswordComponent;
import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import com.neroimor.ImageLibrary.Components.UID.GeneratorUID;
import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final GeneratorUID generatorUID;
    private final AppSettings appSettings;
    private final DataError dataError;
    private final PasswordComponent passwordComponent;

    @Autowired
    public RegisterUserService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               EmailService emailService,
                               GeneratorUID generatorUID,
                               AppSettings appSettings,
                               DataError dataError,
                               PasswordComponent passwordComponent) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.generatorUID = generatorUID;
        this.appSettings = appSettings;
        this.dataError = dataError;
        this.passwordComponent = passwordComponent;
    }

    @Transactional
    public ResponseEntity<String> registerUser(RegisterUser registerUser) {
        log.info("Подключение к базе данных и проверка пользователя на отсутствие");
        try {
            Optional<User> user = userRepository.findByEmail(registerUser.getEmail());
            if (user.isEmpty()) {
                return userRegisterIsEmpty(registerUser);
            } else {
                return userRegisterIsPresent(user, registerUser);
            }
        } catch (DataAccessException e) {
            return dataError.dataAccessError(e);
        }
    }

    private ResponseEntity<String> userRegisterIsEmpty(RegisterUser registerUser) {
        log.info("Пользователь отсутствует, начало регистрации");
        Optional<User> regUser = Optional.ofNullable(registerUserToUser(registerUser));
        if (regUser.isPresent()) {
            String textRegisterAndUID = appSettings
                    .getRegisterResponse()
                    .getConfirmYourEmail()
                    + "\n" + regUser.get()
                    .getCodeuid();
            emailService.sendIntoMail(regUser.get(),
                    appSettings.getRegisterResponse()
                            .getConfirmYourEmailSubject(), textRegisterAndUID);
            log.info("Пользователь зарегистрирован и добавлен в базу данных, нужна проверка");
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    appSettings.getRegisterResponse().getCreatedUser());
        } else {
            log.info("Повторный пароль не совпадает");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    appSettings.getRegisterResponse().getPasswordsDontMatch());
        }
    }

    private ResponseEntity<String> userRegisterIsPresent(Optional<User> user, RegisterUser registerUser) {
        if (!user.get().isVerified()) {
            if (passwordComponent.checkPassword(registerUser.getPassword(), user.get().getPassword())) {
                log.info("Пользователь уже существует. Отправлен новый код");
                String textRegisterAndUID = appSettings
                        .getRegisterResponse()
                        .getConfirmYourEmail()
                        + "\n" + user.get().getCodeuid();
                emailService.sendIntoMail(user.get(),
                        appSettings.getRegisterResponse()
                                .getConfirmYourEmailSubject(), textRegisterAndUID);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        appSettings.getRegisterResponse().getUserFoundAndNotVerified());
            } else {
                log.info("Неверный пароль");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        appSettings.getErrorResponseServer().getUserPasswordNotCorrect());
            }
        } else {
            log.info("Пользователь уже зарегистрирован");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    appSettings.getRegisterResponse().getUserFound());
        }
    }

    @Transactional
    public ResponseEntity<String> userVerified(String email, String codeuid) {
        try {
            log.info("Начата проверка пользователя");
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                log.info("Пользователь найден, проверка кода");
                if (user.get().getCodeuid().equals(codeuid)) {
                    user.get().setVerified(true);
                    userRepository.save(user.get());
                    log.info("Код верен, пользователь подтверждён");
                    return ResponseEntity.status(HttpStatus.OK).body(
                            appSettings.getRegisterResponse().getUserVerified());
                } else {
                    log.info("Код не верен!");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(
                            appSettings.getRegisterResponse().getErrorUid());
                }
            } else {
                log.info("Пользователя нет");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        appSettings.getErrorResponseServer().getUserNotFound());
            }
        } catch (DataAccessException e) {
            return dataError.dataAccessError(e);
        }
    }

    private User registerUserToUser(RegisterUser registerUser) {
        if (registerUser.getPassword().equals(registerUser.getRepitePassword())) {
            var user = new User();
            String finalPassword = passwordEncoder.encode(
                    registerUser.getPassword()+appSettings.getSettingUsers().getSalt());
            user.setPassword(finalPassword);
            user.setNickname(registerUser.getNickname());
            user.setEmail(registerUser.getEmail());
            user.setROLE(appSettings.getSettingUsers().getRole());
            user.setCreated_at(new Date());
            user.setCodeuid(generatorUID.generatorUID());
            log.info("Данные о пользователе переведены");
            return userRepository.save(user);
        }
        return null;
    }
}

