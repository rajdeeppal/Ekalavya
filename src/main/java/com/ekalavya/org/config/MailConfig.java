package com.ekalavya.org.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Base64;

@Configuration
public class MailConfig {

    @Value("${spring.mail.password}")
    private String encodedPassword;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        String decodedPassword = new String(Base64.getDecoder().decode(encodedPassword));
        mailSender.setPassword(decodedPassword);
        return mailSender;
    }
}
