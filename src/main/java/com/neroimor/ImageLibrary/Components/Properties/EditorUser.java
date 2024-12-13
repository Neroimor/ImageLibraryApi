package com.neroimor.ImageLibrary.Components.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "editor-user")
public class EditorUser {
    private SettingProfile settingProfile;

    @Data
    public static class SettingProfile{
        private String renickname;
        private String userError;
        private String validationError;
        private String repassword;
        private String passwordError;
        private String errorVerification;
    }
}
