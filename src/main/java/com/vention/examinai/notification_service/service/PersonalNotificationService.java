package com.vention.examinai.notification_service.service;

import com.vention.examinai.notification_service.dto.request.PersonalNotificationRequest;
import com.vention.examinai.notification_service.dto.response.PersonalNotificationResponse;

public interface PersonalNotificationService {
    PersonalNotificationResponse save(PersonalNotificationRequest personalNotificationRequest);
}
