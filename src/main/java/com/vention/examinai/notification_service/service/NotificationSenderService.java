package com.vention.examinai.notification_service.service;

import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;

public interface NotificationSenderService {
    void sendPersonalNotification(TaskNotificationRequest taskNotificationRequest);

}
