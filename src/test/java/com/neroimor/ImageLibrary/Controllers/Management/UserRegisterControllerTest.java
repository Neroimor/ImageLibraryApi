package com.neroimor.ImageLibrary.Controllers.Management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neroimor.ImageLibrary.Models.UsersModels.RegisterUser;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserRegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testInvalidEmail() throws Exception {
        RegisterUser registerUser = fishUser();
        registerUser.setEmail("invalid-email");
        String requestBody = new ObjectMapper().writeValueAsString(registerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reg/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Некорректный формат email")));
    }

    @Test
    public void testInvalidEmailIsEmpty() throws Exception {
        RegisterUser user = fishUser();
        user.setEmail("");
        String requestBody = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reg/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Поле email не может быть пустым")));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        RegisterUser user = fishUser();
        user.setPassword("fish");
        user.setRepitePassword("fish");
        String requestBody = new ObjectMapper().writeValueAsString(user);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/reg/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Пороль должен быть от 8 до 20 символов")));
    }
    @Test
    public void testInvalidPasswordLanguage() throws Exception {
        RegisterUser user = new RegisterUser();
        user.setPassword("ввффыв");
        user.setRepitePassword("ввффыв");
        String requestBody = new ObjectMapper().writeValueAsString(user);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/reg/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Допустимы только английские буквы, цифры и спецсимволы !@#$%^&")));
    }

    @Test
    public void testInvalidNickname() throws Exception {
        RegisterUser user = fishUser();
        user.setNickname("");
        String requestBody = new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reg/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400
                .andExpect(content().string(containsString("Имя пользователя не может быть пустым"))); // Проверяем ошибку для nickname
    }

    private RegisterUser fishUser() {
        var fishUser = new RegisterUser();
        fishUser.setEmail("fish@fishail.com");
        fishUser.setPassword("fishfish");
        fishUser.setRepitePassword("fishfish");
        fishUser.setNickname("Fish");
        return fishUser;
    }
}