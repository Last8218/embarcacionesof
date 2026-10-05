package com.last.embarcaciones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.username:}") String username,
            @Value("${spring.mail.password:}") String password,
            @Value("${spring.mail.properties.mail.smtp.auth:true}") boolean smtpAuth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}") boolean startTls,
            @Value("${spring.mail.properties.mail.smtp.connectiontimeout:5000}") int connectionTimeout,
            @Value("${spring.mail.properties.mail.smtp.timeout:5000}") int timeout,
            @Value("${spring.mail.properties.mail.smtp.writetimeout:5000}") int writeTimeout) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", String.valueOf(smtpAuth));
        properties.put("mail.smtp.starttls.enable", String.valueOf(startTls));
        properties.put("mail.smtp.connectiontimeout", String.valueOf(connectionTimeout));
        properties.put("mail.smtp.timeout", String.valueOf(timeout));
        properties.put("mail.smtp.writetimeout", String.valueOf(writeTimeout));

        return mailSender;
    }
}

