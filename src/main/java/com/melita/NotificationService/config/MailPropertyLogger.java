package com.melita.NotificationService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class MailPropertyLogger {

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username}")
    private String mailUser;

    @Value("${spring.mail.password}")
    private String mailPass;

    @PostConstruct
    public void logMailProperties() {
        System.out.println("Mail Config Loaded:");
        System.out.println("Host: " + mailHost);
        System.out.println("Port: " + mailPort);
        System.out.println("Username: " + mailUser);
    }
}
