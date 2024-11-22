package com.neroimor.ImageLibrary.Components.UID;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GeneratorUID {
    public String generatorUID(){
        SecureRandom random = new SecureRandom();
        // Генерируем семизначное число от 1000000 до 9999999
        int uid_value =  1_000_000 + random.nextInt(9_000_000);
        return String.valueOf(uid_value);
    }
}
