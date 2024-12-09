package com.neroimor.ImageLibrary.Components.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "setting-admin")
public class AdminSettings {

    private SettingOperation settingOperation;
    private SettingRole settingRole;

    @Data
    public static class SettingOperation {
        private String deleted;
        private String edit;
        private String userNotFound;
    }

    @Data
    public static class SettingRole {
        private String roleAdmin;
        private String roleUser;
        private String userChangeRole;
    }
}
