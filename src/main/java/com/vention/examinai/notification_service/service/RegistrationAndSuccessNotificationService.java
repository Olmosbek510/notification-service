package com.vention.examinai.notification_service.service;

import com.vention.examinai.notification_service.dto.email.ConfirmationEmailDto;
import com.vention.examinai.notification_service.dto.email.RegistrationSuccessEmailDto;

public interface RegistrationAndSuccessNotificationService {
    void sendConfirmationEmail(ConfirmationEmailDto emailDto);
    void sendRegistrationSuccessEmail(RegistrationSuccessEmailDto emailDto);
}
