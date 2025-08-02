package com.vention.examinai.notification_service.dto.request;

public record TaskNotificationRequest(
        String type,
        String recipientId,
        String senderId,
        String subject,
        String message
) {
}
