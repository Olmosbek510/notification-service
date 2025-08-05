package com.vention.examinai.notification_service.service.impl;

import com.vention.examinai.notification_service.config.ExceptionReporter;
import com.vention.examinai.notification_service.dto.request.ErrorNotification;
import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;
import com.vention.examinai.notification_service.service.NotificationSenderService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationSenderServiceImpl implements NotificationSenderService {
    private final JavaMailSender mailSender;
    private final ExceptionReporter exceptionReporter;

    @Override
    public void sendPersonalNotification(TaskNotificationRequest taskNotificationRequest) {
        // todo: fetch sender and recipient details from other service based on their ids
        // todo: integrate with real mail

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(taskNotificationRequest.senderId());
        mailMessage.setTo(taskNotificationRequest.recipientId());
        mailMessage.setSubject(taskNotificationRequest.subject());
        mailMessage.setText(taskNotificationRequest.message());

        mailSender.send(mailMessage);
    }

    @Override
    public void sendTextEmail(String to, String subject, String body) {
        String traceId = MDC.get("traceId");
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            exceptionReporter.report(
                    ErrorNotification.builder()
                            .traceId(traceId)
                            .occurredAt(LocalDateTime.now())
                            .message(e.getMessage())
                            .stackTrace(Arrays.toString(e.getStackTrace()))
                            .exceptionClass(e.getClass().getName())
                            .build()
            );
            e.printStackTrace();
        }
    }

    @Override
    public void sendExcelEmail(String to, String subject, String attachmentName, byte[] excelBytes) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText("Please find attached the latest exception report in Excel format.", false);
            helper.addAttachment(attachmentName, () -> new ByteArrayInputStream(excelBytes));

            mailSender.send(mimeMessage);
            System.out.println("Sent Excel report email to " + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
