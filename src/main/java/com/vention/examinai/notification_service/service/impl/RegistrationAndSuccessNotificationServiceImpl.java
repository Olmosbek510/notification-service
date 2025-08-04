package com.vention.examinai.notification_service.service.impl;

import com.vention.examinai.notification_service.dto.email.ConfirmationEmailDto;
import com.vention.examinai.notification_service.dto.email.RegistrationSuccessEmailDto;
import com.vention.examinai.notification_service.service.RegistrationAndSuccessNotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegistrationAndSuccessNotificationServiceImpl implements RegistrationAndSuccessNotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    public RegistrationAndSuccessNotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendConfirmationEmail(ConfirmationEmailDto emailDto) {
        String subject = "Confirm your registration";
        String body = String.format(
                "Hello, %s!\n\nPlease, confirm your registration by clicking the link:\n%s",
                emailDto.getUsername(),
                emailDto.getConfirmationLink()
        );
        sendSimpleEmail(emailDto.getTo(), subject, body);
    }

    @Override
    public void sendRegistrationSuccessEmail(RegistrationSuccessEmailDto emailDto) {
        String subject = "Registration success";
        String body = String.format(
                "Welcome, %s!\n\nNow you have successfully registered registration!",
                emailDto.getUsername()
        );
        sendSimpleEmail(emailDto.getTo(), subject, body);
    }

    private void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            System.out.printf("Mail was sent to %s%n", to);
        } catch (MailException e) {
            System.err.printf("An error trying to send a mail to %s: %s%n", to, e.getMessage());
            // TODO: записать в лог или в БД
        }
    }
}
