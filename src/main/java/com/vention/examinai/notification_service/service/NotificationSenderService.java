package com.vention.examinai.notification_service.service;

import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;

public interface NotificationSenderService {
    void sendPersonalNotification(TaskNotificationRequest taskNotificationRequest);

    void sendTextEmail(String to, String subject, String body);

    void sendExcelEmail(String to, String subject, String attachmentName, byte[] excelBytes);
}
