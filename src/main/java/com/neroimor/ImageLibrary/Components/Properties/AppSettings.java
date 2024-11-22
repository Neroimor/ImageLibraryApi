package com.neroimor.ImageLibrary.Components.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "settingsRegister")
@Data
public class AppSettings {

    private SettingUsers settingUsers;
    private RegisterResponse registerResponse;
    private ErrorResponseServer errorResponseServer;

    @Data
    public static class SettingUsers {
        private String rolle;
        private String salt;
    }

    @Data
    public static class RegisterResponse {
        private String createdUser;
        private String userFoundAndNotVerefied;
        private String userFound;
        private String userVerefied;
        private String errorUID;
        private String notcorrectPassword;
        private String passwordDontMatch;
    }

    @Data
    public static class ErrorResponseServer {
        private String connectedDB;
        private String userNotFound;
        private String userPasswordNotCorrect;
    }
}