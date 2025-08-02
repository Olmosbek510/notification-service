package com.vention.examinai.notification_service.service.impl;

import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;
import com.vention.examinai.notification_service.service.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSenderServiceImpl implements NotificationSenderService {
    private final JavaMailSender mailSender;

    @Override
    public void sendPersonalNotification(TaskNotificationRequest taskNotificationRequest) {
        // todo: fetch sender and recipient details from other service based on ids
        // todo: integrate with real mail

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(taskNotificationRequest.senderId());
        mailMessage.setTo(taskNotificationRequest.recipientId());
        mailMessage.setSubject(taskNotificationRequest.subject());
        mailMessage.setText(taskNotificationRequest.message());

        mailSender.send(mailMessage);
    }
}
