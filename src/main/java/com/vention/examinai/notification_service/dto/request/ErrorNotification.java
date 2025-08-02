package com.vention.examinai.notification_service.dto.request;

import java.time.LocalDateTime;

public record ErrorNotification(
        String serviceName,
        String exceptionClass,
        String endpoint, // nullable if caused by system
        String statusCode,
        String traceId,
        LocalDateTime occurredAt
) {
}
