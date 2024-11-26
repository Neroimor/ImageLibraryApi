package com.neroimor.ImageLibrary.Components.Password;

import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordComponent {
    private final PasswordEncoder passwordEncoder;
    private final AppSettings appSettings;

    @Autowired
    public PasswordComponent(PasswordEncoder passwordEncoder, AppSettings appSettings) {
        this.passwordEncoder = passwordEncoder;
        this.appSettings = appSettings;
    }
    public boolean checkPassword(String password, String secretPassword) {
        return passwordEncoder.matches(password + appSettings.getSettingUsers().getSalt(), secretPassword);
    }
}
