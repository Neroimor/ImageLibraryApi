package com.neroimor.ImageLibrary.Models.UsersModels;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String nickname;

    @Size(min=8, max = 20, message = "Пороль должен быть от 8 до 20 символов")
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&]+$",
            message = "Допустимы только английские буквы, цифры и спецсимволы !@#$%^&"
    )
    private String password;
    @Size(min=8, max = 20, message = "Пороль должен быть от 8 до 20 символов")
    private String repitePassword;
    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Поле email не может быть пустым")
    private String email;
}
