package com.neroimor.ImageLibrary.Models.UsersModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
    private String nickname;
    private String password;
    private String email;
}
