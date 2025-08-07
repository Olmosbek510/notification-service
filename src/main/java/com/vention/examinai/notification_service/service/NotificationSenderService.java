package com.vention.examinai.notification_service.service;

import com.vention.examinai.notification_service.dto.request.PersonalNotificationRequest;

public interface NotificationSenderService {
    void sendPersonalNotification(PersonalNotificationRequest personalNotificationRequest);

    void sendTextEmail(String to, String subject, String body);

    void sendExcelEmail(String to, String subject, String attachmentName, byte[] excelBytes);
}
