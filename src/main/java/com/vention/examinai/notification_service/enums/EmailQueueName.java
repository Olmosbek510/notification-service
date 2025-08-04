package com.vention.examinai.notification_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailQueueName {
    CONFIRMATION_EMAIL("Confirmation-Email"),
    REGISTRATION_SUCCESS("Registration-Success-Email"),
    CONFIRMATION_DLQ("Confirmation-Dlq"),
    RETRY("Confirmation-Email-Retry");

    private final String value;
}

