package com.neroimor.ImageLibrary.Configuration.Security;

import com.neroimor.ImageLibrary.Components.JWTComponent.JwtAuthenticationFilter;
import com.neroimor.ImageLibrary.Components.JWTComponent.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Класс конфигурации безопасности
//Security configuration class
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    //Подключение бина по генерации и проверки токена
    //Connecting a bean for token generation and verification
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //Подключение кодера пароля
    //Connecting a password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //Настройка фильтра безопасности
    //Security filter settings
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(ign -> ign
                        .ignoringRequestMatchers(
                                "/api/auth/**",  //отключает CSRF-защиту для некоторых эндпоинтов.
                                "/api/reg/**"))//disables CSRF protection for some endpoints.

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/reg/**")// Разрешаем доступ к этим маршрутам без авторизации
                        .permitAll()  // Allow access to these routes without authorization
                        .anyRequest() //Requires authorization
                        .authenticated()  // Все остальные маршруты требуют авторизации
                )
                .exceptionHandling(e -> e // // Status 401 for unauthorized
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))  // Статус 401 для неавторизованных
                )
                .addFilterBefore(jwtAuthenticationFilter(), // Adding a filter for JWT;
                        UsernamePasswordAuthenticationFilter.class); // Добавление фильтра для JWT;
        return http.build();
    }

    //Проверка токена
    //Token verification
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

}
