package com.vention.examinai.notification_service.service.impl;

import com.vention.examinai.notification_service.config.ExceptionReporter;
import com.vention.examinai.notification_service.dto.request.PersonalNotificationRequest;
import com.vention.examinai.notification_service.service.NotificationSenderService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationSenderServiceImpl implements NotificationSenderService {
    private final JavaMailSender mailSender;
    private final ExceptionReporter exceptionReporter;

    @Override
    public void sendPersonalNotification(PersonalNotificationRequest personalNotificationRequest) {
        log.info("Sending personal notification from user ID: {} to user ID: {}",
                personalNotificationRequest.senderId(), personalNotificationRequest.recipientId());

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("examinai.notifications@gmail.com"); // hardcoded
            mailMessage.setTo("jw017306@gmail.com"); // // hardcoded
            mailMessage.setSubject(personalNotificationRequest.subject());
            mailMessage.setText(personalNotificationRequest.message());

            mailSender.send(mailMessage);

            log.info("Personal notification email sent successfully.");
        } catch (Exception e) {
            exceptionReporter.report(e);
            log.error("Failed to send personal notification email. Error: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendTextEmail(String to, String subject, String body) {
        log.info("Sending text email to: {}, subject: {}", to, subject);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            helper.setFrom("examinai.notifications@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(mimeMessage);

            log.info("Text email sent successfully to: {}", to);
        } catch (Exception e) {
            exceptionReporter.report(e);
            log.error("Failed to send text email to: {}. Error: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendExcelEmail(String to, String subject, String attachmentName, byte[] excelBytes) {
        log.info("Sending Excel report email to: {}, subject: {}", to, subject);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setFrom("examinai.notifications@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText("Please find attached the latest exception report in Excel format.", false);
            helper.addAttachment(attachmentName, () -> new ByteArrayInputStream(excelBytes));

            mailSender.send(mimeMessage);

            log.info("Excel report email sent successfully to: {}", to);
        } catch (Exception e) {
            exceptionReporter.report(e);
            log.error("Failed to send Excel email to: {}. Error: {}", to, e.getMessage(), e);
        }
    }

}
