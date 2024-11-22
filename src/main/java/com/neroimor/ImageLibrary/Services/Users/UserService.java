package com.neroimor.ImageLibrary.Services.Users;

import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UserService {
    @Value("${setingUsers.rolle}")
    private String role;
    @Value("${registerResponse.createdUser}")
    private String userRegister;
    @Value("${registerResponse.userFoundAndNotVerefied}")
    private String userFoundAndNotVerefied;
    @Value("${registerResponse.userFound}")
    private String userFound;
    @Value("${errorResponseServer.conectedDB}")
    private String connectedDB;
    @Value("${setingUsers.salt}")
    private String saltPassword;
    @Value("${textEmailRegister.subject}")
    private String subjectRegister;
    @Value("${textEmailRegister.text}")
    private String registerText;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /*
    public User registerUser(String email, String password) {
      //  User user = new User();
      //  user.setEmail(email);
      //  user.setPassword(passwordEncoder.encode(password));
      //  user.setProvider("LOCAL");
      //  log.info("Registering user: {}", user);
        return userRepository.save(user);
    }
    */
    @Transactional
    public ResponseEntity<String> registerUser(RegisterUser registerUser) {
        log.info("Подключение к базе данных и проверка пользователя на отсутсвиее");
        try {
            Optional<User> user = userRepository.findByEmail(registerUser.getEmail());

            if (user.isEmpty()) {
                log.info("Пользователь отсутсвует, начало регистрации");
                var regUser = registerUserToUser(registerUser);
                String textRegisterAndUID = registerText.concat("\n"+regUser.getCodeuid());
                sendUidIntoMail(regUser,subjectRegister,textRegisterAndUID);
                log.info("Пользовватель зарегестрирован и добавлен в базу данных, нужна проверка");
                return ResponseEntity.status(HttpStatus.CREATED).body(userRegister);
            } else {
                if (user.get().isVerified()) {
                    log.info("Пользовватель уже существует. Отправлен новый код");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(userFoundAndNotVerefied);
                } else {
                    log.info("Пользователь уже зарегестрирован");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(userFound);
                }
            }
        }
        catch (DataAccessException e){
            return dataAccessErorr(e);
        }
    }


    private User registerUserToUser(RegisterUser registerUser) {
        var user = new User();
        String finalPassword = passwordEncoder.encode(registerUser.getPassword()+saltPassword);
        user.setPassword(finalPassword);
        user.setNickname(registerUser.getNickname());
        user.setEmail(registerUser.getEmail());
        user.setROLE(role);
        user.setCreated_at(new Date());

        log.info("Данные о пользователе переведены");
        return userRepository.save(user);
    }

    private void sendUidIntoMail(User user,String subject,String message) {
        log.info("Отрпалвено письмо о {}", subject);
        emailService.sendSimpleEmail(
                user.getEmail(),
                subject,
                message
        );
    }


    private ResponseEntity<String> dataAccessErorr(DataAccessException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(connectedDB+"\n"+e.getMessage());
    }
}
