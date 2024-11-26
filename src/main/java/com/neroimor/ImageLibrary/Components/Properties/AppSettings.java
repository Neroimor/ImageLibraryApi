package com.neroimor.ImageLibrary.Components.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "settings-register")
@Data
public class AppSettings {
    private SettingUsers settingUsers;
    private RegisterResponse registerResponse;
    private ErrorResponseServer errorResponseServer;
    private LoginMoment loginMoment;


    @Data
    public static class SettingUsers {
        private String role;
        private String salt;

    }

    @Data
    public static class RegisterResponse {
        private String createdUser;
        private String userFoundAndNotVerified;
        private String userFound;
        private String userVerified;
        private String errorUid;
        private String incorrectPassword;
        private String passwordsDontMatch;
        private String confirmYourEmail;
        private String confirmYourEmailSubject;

    }

    @Data
    public static class ErrorResponseServer {
        private String connectedDb;
        private String userNotFound;
        private String userPasswordNotCorrect;

    }

    @Data
    public static class LoginMoment {
        private String unauthorized;
        private String userNotFound;
        private String userNotVerified;
    }
}