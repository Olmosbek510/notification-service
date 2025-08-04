package com.vention.examinai.notification_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailExchange {
    DIRECT_EMAIL("Direct-Email-Exchange");

    private final String value;
}


