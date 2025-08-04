package com.vention.examinai.notification_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailRoutingKey {
    CONFIRMATION_EMAIL("confirmation-email"),
    REGISTRATION_SUCCESS("registration-success-email"),
    CONFIRMATION_DLQ("confirmation-dlq"),
    RETRY("email.confirmation.retry");

    private final String value;
}
