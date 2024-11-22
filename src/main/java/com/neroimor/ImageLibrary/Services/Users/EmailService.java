package com.neroimor.ImageLibrary.Services.Users;

import com.neroimor.ImageLibrary.Models.UsersModels.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailSenderAddress;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(emailSenderAddress);
        mailSender.send(message);
    }


    public void sendIntoMail(User user, String subject, String message) {
        log.info("Отправлено письмо о {}", subject);
        sendSimpleEmail(
                user.getEmail(),
                subject,
                message
        );
    }
}
