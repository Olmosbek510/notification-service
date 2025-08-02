package com.vention.examinai.notification_service.service;

import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;

public interface PersonalNotificationService {
    Long save(TaskNotificationRequest taskNotificationRequest);
}
