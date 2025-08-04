package com.vention.examinai.notification_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorNotification {
    private String serviceName;
    private String exceptionClass;
    private String endpoint; // nullable if caused by system
    private String statusCode;
    private String traceId;
    private String message;
    private String stackTrace;
    private LocalDateTime occurredAt;
}
